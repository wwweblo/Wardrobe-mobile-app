package com.example.coursework.data

import androidx.lifecycle.LiveData

/*
*   Репозиторий - класс, который предоставляет достум к нескольким източникам данных
*   Он не обязателен, но это правило хорошего тона
*/
class ClothesRepository(private val dao: ClothesDao) {

    val readAllData: LiveData<List<ClothingItem>> = dao.getClothingItemsSortedById()

    suspend fun addClothingItem(clothingItem: ClothingItem){
        dao.addClothingItem(clothingItem)
    }

}