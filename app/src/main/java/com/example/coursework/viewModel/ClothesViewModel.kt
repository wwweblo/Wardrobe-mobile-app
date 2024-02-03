package com.example.coursework.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coursework.data.ClothesDatabase
import com.example.coursework.repository.ClothesRepository
import com.example.coursework.model.ClothingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SortOrder {
    ASCENDING,
    DESCENDING
}

class ClothesViewModel(application: Application): AndroidViewModel(application) {

    private val _readAllData = MutableLiveData<List<ClothingItem>>()
    val readAllData: LiveData<List<ClothingItem>> get() = _readAllData

    private val repository: ClothesRepository

    init {
        val dao = ClothesDatabase.getDatabase(application).dao()
        repository = ClothesRepository(dao)
        getSortedClothingItems()
    }

    fun addClothingItem(clothingItem: ClothingItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClothingItem(clothingItem)
        }
    }

    fun updateClothingItem(clothingItem: ClothingItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateClothingItem(clothingItem)
        }
    }

    fun deleteClothingItem(clothingItem: ClothingItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteClothingItem(clothingItem)
        }
    }

    private var sortOrder = SortOrder.ASCENDING // По умолчанию сортировка в возрастающем порядке
    fun toggleSortOrder() {
        sortOrder = if (sortOrder == SortOrder.ASCENDING) {
            SortOrder.DESCENDING
        } else {
            SortOrder.ASCENDING
        }
        getSortedClothingItems()
    }

    private fun getSortedClothingItems() {
        viewModelScope.launch(Dispatchers.IO) {
            // Получите данные из репозитория с учетом sortOrder
            val sortedItems = repository.getAllClothingItemsSorted(sortOrder)
            // Обновите LiveData
            _readAllData.postValue(sortedItems.value)
        }
    }


    fun deleteEveryClothingItem(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEveryClothingItem()
        }
    }
}
