package com.example.coursework.repository

import androidx.lifecycle.LiveData
import com.example.coursework.data.dao.ClothesDao
import com.example.coursework.model.ClothingItem

/*
*   Репозиторий - класс, который предоставляет достум к нескольким източникам данных
*   Он не обязателен, но это правило хорошего тона.
*
*   В нем прописываются:
*   Корутины
*   Связи между базой данных и ViewModel
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

    fun getClothingItemsSortedByTitle(): LiveData<List<ClothingItem>> {
        return dao.getClothingItemsSortedByTitle()
    }

    fun getClothingItemsSortedByDateUpdated(): LiveData<List<ClothingItem>> {
        return dao.getClothingItemsSortedByDateUpdated()
    }

    fun isImagePathUsed(imagePath: String?): Boolean {
        return dao.isImagePathUsed(imagePath)
    }
}