package com.example.coursework.repository

import androidx.lifecycle.LiveData
import com.example.coursework.data.dao.ClothingItemOutfitCrossRefDao
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit

class ClothingItemOutfitCrossRefRepository(private val dao: ClothingItemOutfitCrossRefDao) {

    // Добавление новой связи между элементом одежды и комплектом
    suspend fun addClothingItemOutfitCrossRef(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef) {
        dao.insert(clothingItemOutfitCrossRef)
    }

    // Удаление связи между элементом одежды и комплектом
    suspend fun deleteClothingItemOutfitCrossRef(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef) {
        dao.delete(clothingItemOutfitCrossRef)
    }

    // Получение всех элементов одежды для конкретного комплекта
    fun getClothingItemsForOutfit(outfitId: Int): LiveData<List<ClothingItem>> {
        return dao.getClothingItemsForOutfit(outfitId)
    }

    // Получение всех комплектов для конкретного элемента одежды
    fun getOutfitsForClothingItem(clothingItemId: Int): LiveData<List<Outfit>> {
        return dao.getOutfitsForClothingItem(clothingItemId)
    }

    //Удаление всязи для конкретного элемента Outfit
    fun deleteCrossRefsForOutfit(outfitId: Int) {
        return dao.deleteCrossRefsForOutfit(outfitId)
    }

    // Функция для удаления всех перекрестных ссылок для определенного ClothingItem
    fun deleteCrossRefsForClothingItem(clothingItemId: Int) {
        dao.deleteCrossRefsForClothingItem(clothingItemId)
    }
}
