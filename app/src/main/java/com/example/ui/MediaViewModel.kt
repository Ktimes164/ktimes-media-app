package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DefaultData
import com.example.data.MediaItem
import com.example.data.MediaRepository
import com.example.data.firebase.FirebaseAuthService
import com.example.data.firebase.FirestoreService
import com.example.data.models.AdOrder
import com.example.data.models.AppUser
import com.example.data.models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MediaRepository
    val authService = FirebaseAuthService()
    val firestoreService = FirestoreService()

    val selectedCategory = MutableStateFlow("All")
    val selectedMediaType = MutableStateFlow("ALL") // "ALL", "AUDIO", "VIDEO", "GRAPHIC"
    val searchQuery = MutableStateFlow("")
    val currentTab = MutableStateFlow("home") // "home", "portfolio", "rates", "orders"

    val selectedItemForDetail = MutableStateFlow<MediaItem?>(null)
    val itemToEdit = MutableStateFlow<MediaItem?>(null)

    val adminMessage = MutableStateFlow<String?>(null)
    val authStatusMessage = MutableStateFlow<String?>(null)

    // Auth & Role states
    val currentUser: StateFlow<AppUser?> = authService.currentUserState
    val currentUserRole: StateFlow<UserRole> = authService.currentRole

    // Real-time Orders / Leads Pipeline from Firestore
    val ordersList: StateFlow<List<AdOrder>> = firestoreService.observeOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = firestoreService.initialMockOrders
        )

    init {
        val dao = AppDatabase.getDatabase(application).mediaItemDao()
        repository = MediaRepository(dao)

        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    val filteredItems: StateFlow<List<MediaItem>> = combine(
        repository.allItems,
        selectedCategory,
        selectedMediaType,
        searchQuery
    ) { items, category, type, query ->
        items.filter { item ->
            val matchesCategory = (category == "All" || item.category.equals(category, ignoreCase = true))
            val matchesType = (type == "ALL" || item.mediaType.equals(type, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.sampleId.contains(query, ignoreCase = true) ||
                    item.tags.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)

            matchesCategory && matchesType && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTabSelected(tab: String) {
        currentTab.value = tab
    }

    fun navigateToCategoryInPortfolio(category: String, type: String = "ALL") {
        selectedCategory.value = category
        selectedMediaType.value = type
        currentTab.value = "portfolio"
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value = category
    }

    fun onMediaTypeSelected(type: String) {
        selectedMediaType.value = type
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun openItemDetail(item: MediaItem) {
        selectedItemForDetail.value = item
    }

    fun closeItemDetail() {
        selectedItemForDetail.value = null
    }

    fun startEditingItem(item: MediaItem?) {
        itemToEdit.value = item
    }

    // Role and Auth methods
    fun switchUserRole(role: UserRole) {
        authService.setExplicitRole(role)
        authStatusMessage.value = if (role == UserRole.ADMIN) "Switched to Admin Role" else "Switched to Client Role"
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            val result = authService.signInWithGoogle(context)
            result.onSuccess { user ->
                authStatusMessage.value = "Signed in as ${user.displayName} (${user.role})"
            }.onFailure { error ->
                authStatusMessage.value = "Sign in note: Switched active session"
            }
        }
    }

    fun signInWithEmail(email: String, pass: String) {
        viewModelScope.launch {
            val result = authService.signInWithEmail(email, pass)
            result.onSuccess { user ->
                authStatusMessage.value = "Welcome back, ${user.displayName}!"
            }.onFailure { error ->
                authStatusMessage.value = "Authentication failed: ${error.message}"
            }
        }
    }

    fun signOut() {
        authService.signOut()
        authStatusMessage.value = "Logged out"
    }

    // Order and CRM Actions (Firestore synced)
    fun submitAdOrder(
        clientName: String,
        clientPhone: String,
        serviceType: String,
        budget: String,
        details: String,
        location: String = "Satara"
    ) {
        viewModelScope.launch {
            val orderId = "KTM-ORD-${System.currentTimeMillis().toString().takeLast(4)}"
            val user = currentUser.value
            val newOrder = AdOrder(
                id = orderId,
                clientUid = user?.uid ?: "client_${System.currentTimeMillis()}",
                clientName = clientName.ifBlank { user?.displayName ?: "Client" },
                clientPhone = clientPhone.ifBlank { user?.phoneNumber ?: "9422337471" },
                businessName = user?.businessName ?: clientName,
                serviceType = serviceType,
                budget = budget,
                details = details,
                location = location,
                status = "New Lead",
                progress = 10,
                timestamp = System.currentTimeMillis()
            )

            val res = firestoreService.createOrder(newOrder)
            if (res.isSuccess) {
                adminMessage.value = "Order $orderId created & synced to Firestore!"
            } else {
                adminMessage.value = "Order $orderId saved locally."
            }
        }
    }

    fun updateOrderStatus(
        orderId: String,
        newStatus: String,
        progress: Int? = null,
        revisionNotes: String? = null
    ) {
        viewModelScope.launch {
            firestoreService.updateOrderStatus(orderId, newStatus, progress, revisionNotes)
            adminMessage.value = "Updated $orderId status to: $newStatus"
        }
    }

    fun advanceOrderStage(order: AdOrder) {
        val stages = listOf("New Lead", "Requirement Received", "Quotation Sent", "Production", "Approval", "Delivered")
        val curIdx = stages.indexOf(order.status)
        if (curIdx >= 0 && curIdx < stages.size - 1) {
            val nextStage = stages[curIdx + 1]
            val nextProgress = when (nextStage) {
                "Requirement Received" -> 30
                "Quotation Sent" -> 50
                "Production" -> 70
                "Approval" -> 90
                "Delivered" -> 100
                else -> order.progress + 15
            }
            updateOrderStatus(order.id, nextStage, nextProgress)
        }
    }

    fun saveItem(item: MediaItem) {
        viewModelScope.launch {
            if (item.id == 0) {
                repository.insertItem(item)
                adminMessage.value = "Added new portfolio sample: ${item.sampleId}"
            } else {
                repository.updateItem(item)
                adminMessage.value = "Updated portfolio sample: ${item.sampleId}"
            }
            // Sync with Firestore
            firestoreService.savePortfolioItem(item)
        }
    }

    fun deleteItem(item: MediaItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
            firestoreService.deletePortfolioItem(item.sampleId)
            adminMessage.value = "Deleted sample: ${item.sampleId}"
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            repository.resetToDefault()
            adminMessage.value = "Reset portfolio to default samples."
        }
    }

    suspend fun exportJson(items: List<MediaItem>): String {
        return repository.exportToJson(items)
    }

    fun importJson(json: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.importFromJson(json)
            if (success) {
                adminMessage.value = "Portfolio successfully imported!"
            } else {
                adminMessage.value = "Error parsing JSON format."
            }
            onResult(success)
        }
    }

    fun clearAdminMessage() {
        adminMessage.value = null
    }

    fun clearAuthStatusMessage() {
        authStatusMessage.value = null
    }
}
