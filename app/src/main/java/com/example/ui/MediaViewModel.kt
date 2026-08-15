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

    // CRM Lead Pipeline Filter States
    val crmSearchQuery = MutableStateFlow("")
    val crmSelectedStage = MutableStateFlow("ALL") // "ALL", "New Lead", "Requirement Received", "Quotation Sent", "Production", "Approval", "Delivered"

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

    val crmFilteredOrders: StateFlow<List<AdOrder>> = combine(
        ordersList,
        crmSearchQuery,
        crmSelectedStage
    ) { orders, query, stage ->
        orders.filter { order ->
            val matchesStage = (stage == "ALL" || order.status.equals(stage, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    order.clientName.contains(query, ignoreCase = true) ||
                    order.clientPhone.contains(query, ignoreCase = true) ||
                    order.serviceType.contains(query, ignoreCase = true) ||
                    order.businessName.contains(query, ignoreCase = true) ||
                    order.id.contains(query, ignoreCase = true) ||
                    order.details.contains(query, ignoreCase = true) ||
                    order.location.contains(query, ignoreCase = true)

            matchesStage && matchesQuery
        }
    }.stateIn(
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
        businessName: String = "",
        deadline: String = "३ दिवस",
        location: String = "Satara",
        onOrderCreated: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val orderId = "KTM-ORD-${System.currentTimeMillis().toString().takeLast(4)}"
            val user = currentUser.value
            val newOrder = AdOrder(
                id = orderId,
                clientUid = user?.uid ?: "client_${System.currentTimeMillis()}",
                clientName = clientName.ifBlank { user?.displayName ?: "ग्राहक (App User)" },
                clientPhone = clientPhone.ifBlank { user?.phoneNumber ?: "9422337471" },
                businessName = businessName.ifBlank { user?.businessName ?: if (clientName.isNotBlank()) clientName else "Ktimes Media Client" },
                serviceType = serviceType,
                budget = budget,
                details = details,
                location = location,
                status = "New Lead",
                progress = 15,
                deadline = deadline.ifBlank { "३ दिवस" },
                timestamp = System.currentTimeMillis()
            )

            val res = firestoreService.createOrder(newOrder)
            if (res.isSuccess) {
                adminMessage.value = "Lead $orderId created & synced to Firestore!"
            } else {
                adminMessage.value = "Lead $orderId saved locally."
            }
            onOrderCreated?.invoke(orderId)
        }
    }

    // Automatically create CRM Lead from Portfolio Item Order
    fun createLeadFromItem(item: MediaItem, customNotes: String = "") {
        val user = currentUser.value
        val clientName = user?.displayName ?: "ॲप वापरकर्ता"
        val clientPhone = user?.phoneNumber ?: "9422337471"
        val serviceType = item.category.ifBlank { item.mediaType }
        val budget = item.priceOrEstimate.ifBlank { "₹१,९९९" }
        val details = "नमुना: ${item.sampleId} - ${item.title}" + if (customNotes.isNotBlank()) " | टीप: $customNotes" else ""

        submitAdOrder(
            clientName = clientName,
            clientPhone = clientPhone,
            serviceType = serviceType,
            budget = budget,
            details = details,
            location = "Satara / Online"
        )
    }

    // Automatically create CRM Lead from Rate Card / Package selection
    fun createLeadFromPackage(packageName: String, price: String) {
        val user = currentUser.value
        val clientName = user?.displayName ?: "ॲप वापरकर्ता"
        val clientPhone = user?.phoneNumber ?: "9422337471"

        submitAdOrder(
            clientName = clientName,
            clientPhone = clientPhone,
            serviceType = packageName,
            budget = price,
            details = "रेट कार्ड पॅकेज चौकशी / ऑर्डर: $packageName ($price)",
            location = "Satara / Online"
        )
    }

    // Automatically create CRM Lead from Home Hero Banner
    fun createLeadFromBanner(bannerTitle: String = "२४/७ डिजिटल स्टुडिओ विशेष ऑफर") {
        val user = currentUser.value
        val clientName = user?.displayName ?: "ॲप वापरकर्ता"
        val clientPhone = user?.phoneNumber ?: "9422337471"

        submitAdOrder(
            clientName = clientName,
            clientPhone = clientPhone,
            serviceType = "ऑडिओ/व्हिडिओ जाहिरात पॅकेज",
            budget = "₹१,९९९ - ₹४,९९९",
            details = "मुख्य बॅनरवरून आलेली थेट चौकशी ($bannerTitle)",
            location = "Satara / Online"
        )
    }

    // Manual Lead Creation from Admin Console
    fun createManualLead(
        clientName: String,
        clientPhone: String,
        businessName: String,
        serviceType: String,
        budget: String,
        deadline: String,
        details: String,
        location: String
    ) {
        viewModelScope.launch {
            val orderId = "KTM-ORD-${System.currentTimeMillis().toString().takeLast(4)}"
            val lead = AdOrder(
                id = orderId,
                clientUid = "admin_created_${System.currentTimeMillis()}",
                clientName = clientName,
                clientPhone = clientPhone,
                businessName = businessName.ifBlank { clientName },
                serviceType = serviceType,
                budget = budget,
                details = details,
                location = location.ifBlank { "Satara" },
                status = "New Lead",
                progress = 15,
                deadline = deadline.ifBlank { "३ दिवस" },
                timestamp = System.currentTimeMillis()
            )
            firestoreService.createOrder(lead)
            adminMessage.value = "New Lead $orderId successfully added to CRM!"
        }
    }

    fun setOrderStage(orderId: String, newStage: String) {
        val nextProgress = when (newStage) {
            "New Lead" -> 15
            "In Review", "Requirement Received" -> 35
            "Confirmed", "Quotation Sent" -> 55
            "Production" -> 75
            "Approval" -> 90
            "Delivered" -> 100
            else -> 20
        }
        updateOrderStatus(orderId, newStage, nextProgress)
    }

    fun updateFullOrder(order: AdOrder) {
        viewModelScope.launch {
            firestoreService.updateFullOrder(order)
            adminMessage.value = "Lead ${order.id} updated successfully."
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            firestoreService.deleteOrder(orderId)
            adminMessage.value = "Lead $orderId removed from CRM."
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
        val stages = listOf("New Lead", "In Review", "Confirmed", "Production", "Approval", "Delivered")
        val currentMappedStatus = when (order.status) {
            "Requirement Received" -> "In Review"
            "Quotation Sent" -> "Confirmed"
            else -> order.status
        }
        val curIdx = stages.indexOf(currentMappedStatus)
        if (curIdx >= 0 && curIdx < stages.size - 1) {
            val nextStage = stages[curIdx + 1]
            val nextProgress = when (nextStage) {
                "In Review" -> 35
                "Confirmed" -> 55
                "Production" -> 75
                "Approval" -> 90
                "Delivered" -> 100
                else -> order.progress + 15
            }
            updateOrderStatus(order.id, nextStage, nextProgress)
        }
    }

    fun onCrmSearchQueryChanged(query: String) {
        crmSearchQuery.value = query
    }

    fun onCrmStageSelected(stage: String) {
        crmSelectedStage.value = stage
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
