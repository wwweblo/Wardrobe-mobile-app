package com.example.coursework.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.coursework.data.ClothesDatabase
import com.example.coursework.repository.ClothesRepository
import com.example.coursework.model.ClothingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
*   Цель ViewModel - передавать данные из базы данных в ui
*   Для отображения изменений. ViewModel нужна для объединения
*   репозитория с ui
*/

class ClothesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ClothingItem>>
    private val repository: ClothesRepository

    init {

        /*
        *   При инициализации:
        *   Мы получаем dao из базы данных
        *   Передаем в репозиторий dao
        *   Получаем все записи из репозитория
        */

        val dao = ClothesDatabase.getDatabase(application).dao()
        repository = ClothesRepository(dao)
        readAllData = repository.readAllData
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

}