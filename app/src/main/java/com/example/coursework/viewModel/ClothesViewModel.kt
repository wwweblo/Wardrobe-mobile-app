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

/*
*   Цель ViewModel - Работа с пользовательским интерфейсом
*/

class ClothesViewModel(application: Application): AndroidViewModel(application) {

    var readAllData: LiveData<List<ClothingItem>> = MutableLiveData<List<ClothingItem>>()
    private val repository: ClothesRepository
    var currentSortType: SortType = SortType.BY_DATE_UPDATED

    init {

        /*
        *   При инициализации:
        *   Мы получаем dao из базы данных
        *   Передаем в репозиторий dao
        *   Устанавливаем readAllData в зависимости от currentSortType
        */

        val dao = ClothesDatabase.getDatabase(application).dao()
        repository = ClothesRepository(dao)

        readAllData = when (currentSortType) {
            SortType.BY_TITLE -> repository.getClothingItemsSortedByTitle()
            SortType.BY_DATE_UPDATED -> repository.getClothingItemsSortedByDateUpdated()
        }
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

    fun deleteEveryClothingItem(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEveryClothingItem()
        }
    }

    fun changeSortType() {
        currentSortType = when (currentSortType) {
            SortType.BY_TITLE -> SortType.BY_DATE_UPDATED
            SortType.BY_DATE_UPDATED -> SortType.BY_TITLE
        }
        updateClothingItemsSortedBy(currentSortType)
    }
    fun updateClothingItemsSortedBy(sortType: SortType) {
        currentSortType = sortType
        readAllData = when (sortType) {
            SortType.BY_TITLE -> repository.getClothingItemsSortedByTitle()
            SortType.BY_DATE_UPDATED -> repository.getClothingItemsSortedByDateUpdated()
        }
    }

    fun isImagePathUsed(imagePath: String?): Boolean {
        return repository.isImagePathUsed(imagePath)
    }

}