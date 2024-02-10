package com.example.coursework.repository

import androidx.lifecycle.LiveData
import com.example.coursework.data.dao.OutfitDao
import com.example.coursework.model.Outfit

class OutfitRepository(private val dao: OutfitDao) {

    // Добавление нового комплекта
    suspend fun addOutfit(outfit: Outfit) {
        dao.insert(outfit)
    }

    // Удаление комплекта
    suspend fun deleteOutfit(outfit: Outfit) {
        dao.delete(outfit)
    }

    // Обновление комплекта
    suspend fun updateOutfit(outfit: Outfit) {
        dao.update(outfit)
    }

    // Получение всех комплектов, отсортированных по названию
    fun getAllOutfitsSortedByName(): LiveData<List<Outfit>> {
        return dao.getAllOutfitsSortedByName()
    }

    // Получение всех комплектов, отсортированных по дате изменения
    fun getAllOutfitsSortedByDate(): LiveData<List<Outfit>> {
        return dao.getAllOutfitsSortedByDate()
    }
}
