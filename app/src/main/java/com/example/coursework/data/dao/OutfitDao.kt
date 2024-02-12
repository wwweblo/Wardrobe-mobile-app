package com.example.coursework.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.coursework.model.Outfit

@Dao
interface OutfitDao {
    // Добавление нового комплекта
    @Insert
    suspend fun insert(outfit: Outfit)

    // Удаление комплекта
    @Delete
    suspend fun delete(outfit: Outfit)

    // Обновление комплекта
    @Update
    suspend fun update(outfit: Outfit)

    // Получение всех комплектов, отсортированных по названию
    @Query("SELECT * FROM Outfit ORDER BY title ASC")
    fun getAllOutfitsSortedByTitle(): LiveData<List<Outfit>>

    // Получение всех комплектов, отсортированных по дате изменения (в порядке убывания)
    @Query("SELECT * FROM Outfit ORDER BY dateUpdated DESC")
    fun getAllOutfitsSortedByDate(): LiveData<List<Outfit>>

    @Query("SELECT COUNT(*) FROM Outfit WHERE image = :imagePath")
    fun isImagePathUsed(imagePath: String?): Boolean
}