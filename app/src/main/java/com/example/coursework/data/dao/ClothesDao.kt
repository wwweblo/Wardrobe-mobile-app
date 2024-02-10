package com.example.coursework.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coursework.model.ClothingItem

/*
*   Dao - Data Access Object
*   Содержит методы для доступа к базе данных
*/

@Dao
interface ClothesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClothingItem(clothingItem: ClothingItem)

    @Delete
    suspend fun deleteClothingItem(clothingItem: ClothingItem)

    @Query("DELETE FROM ClothingItem")
    suspend fun deleteEveryClothingItem()
    @Update
    suspend fun updateClothingItem(clothingItem: ClothingItem)
    @Query("SELECT * FROM ClothingItem ORDER BY id ")
    fun getClothingItemsSortedById():LiveData<List<ClothingItem>>

    @Query("SELECT * FROM ClothingItem ORDER BY title ASC ")
    fun getClothingItemsSortedByTitle():LiveData<List<ClothingItem>>

    @Query("SELECT * FROM ClothingItem ORDER BY dateUpdated DESC")
    fun getClothingItemsSortedByDateUpdated(): LiveData<List<ClothingItem>>

    @Query("SELECT COUNT(*) FROM ClothingItem WHERE image = :imagePath")
    fun isImagePathUsed(imagePath: String?): Boolean
}