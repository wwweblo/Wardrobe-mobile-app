package com.example.coursework.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("UPDATE ClothingItem SET isSelected = NOT isSelected WHERE id = :id")
    suspend fun toggleClothingItemSelection(id: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM ClothingItem WHERE isSelected = 1)")
    fun isAnyItemSelected(): Boolean

    @Query("SELECT COUNT(*) FROM ClothingItem WHERE image = :imagePath")
    fun isImagePathUsed(imagePath: String?): Boolean

    @Transaction    //Transaction нужен для того, чтобы запрос выполнялся в 1 транзакции. Полезно для больших запросов
    @Query("SELECT * FROM ClothingItem INNER JOIN ClothingItemOutfitCrossRef ON ClothingItem.id = ClothingItemOutfitCrossRef.clothingItemId WHERE ClothingItemOutfitCrossRef.outfitId = :outfitId")
    fun getClothingItemsForOutfit(outfitId: Int): LiveData<List<ClothingItem>>
}