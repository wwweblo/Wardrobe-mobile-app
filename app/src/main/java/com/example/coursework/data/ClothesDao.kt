package com.example.coursework.data

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

    @Query("DELETE FROM Clothing_item")
    suspend fun deleteEveryClothingItem()
    @Update
    suspend fun updateClothingItem(clothingItem: ClothingItem)
    @Query("SELECT * FROM Clothing_item ORDER BY id ")
    fun getClothingItemsSortedById():LiveData<List<ClothingItem>>

    @Query("SELECT * FROM Clothing_item ORDER BY title ASC ")
    fun getClothingItemsSortedByTitle():LiveData<List<ClothingItem>>

    @Query("SELECT * FROM clothing_item ORDER BY updated_at DESC")
    fun getAllClothingItemsDescending(): LiveData<List<ClothingItem>>


    @Query("SELECT * FROM clothing_item ORDER BY updated_at ASC")
    fun getAllClothingItemsAscending(): LiveData<List<ClothingItem>>

}