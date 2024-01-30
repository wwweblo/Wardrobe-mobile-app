package com.example.coursework.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
        /*
        *   viewModelScope - для многопоточности
        *   Dispatchers.IO - запускает в фоновом потоке
        */
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClothingItem(clothingItem)
        }
    }

}