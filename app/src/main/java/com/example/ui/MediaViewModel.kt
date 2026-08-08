package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DefaultData
import com.example.data.MediaItem
import com.example.data.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MediaRepository
    val selectedCategory = MutableStateFlow("All")
    val selectedMediaType = MutableStateFlow("ALL") // "ALL", "AUDIO", "VIDEO", "GRAPHIC"
    val searchQuery = MutableStateFlow("")
    val currentTab = MutableStateFlow("home") // "home", "portfolio", "rates", "orders"

    val selectedItemForDetail = MutableStateFlow<MediaItem?>(null)
    val itemToEdit = MutableStateFlow<MediaItem?>(null)

    val adminMessage = MutableStateFlow<String?>(null)

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

    fun saveItem(item: MediaItem) {
        viewModelScope.launch {
            if (item.id == 0) {
                repository.insertItem(item)
                adminMessage.value = "Added new portfolio sample: ${item.sampleId}"
            } else {
                repository.updateItem(item)
                adminMessage.value = "Updated portfolio sample: ${item.sampleId}"
            }
        }
    }

    fun deleteItem(item: MediaItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
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
}
