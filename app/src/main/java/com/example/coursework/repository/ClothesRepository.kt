package com.example.coursework.repository

import androidx.lifecycle.LiveData
import com.example.coursework.data.ClothesDao
import com.example.coursework.model.ClothingItem

/*
*   Репозиторий - класс, который предоставляет достум к нескольким източникам данных
*   Он не обязателен, но это правило хорошего тона
*/
class ClothesRepository(private val dao: ClothesDao) {

    val readAllData: LiveData<List<ClothingItem>> = dao.getClothingItemsSortedById()

    suspend fun addClothingItem(clothingItem: ClothingItem){
        dao.addClothingItem(clothingItem)
    }

    suspend fun updateClothingItem(clothingItem: ClothingItem){
        dao.updateClothingItem(clothingItem)
    }

    suspend fun deleteClothingItem(clothingItem: ClothingItem){
        dao.deleteClothingItem(clothingItem)
    }

    suspend fun deleteEveryClothingItem(){
        dao.deleteEveryClothingItem()
    }
}