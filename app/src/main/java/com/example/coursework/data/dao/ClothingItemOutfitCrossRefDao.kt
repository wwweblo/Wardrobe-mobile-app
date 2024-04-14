package com.example.coursework.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit

@Dao
interface ClothingItemOutfitCrossRefDao {
    // Добавление новой связи между элементом одежды и комплектом
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef)

    // Удаление связи между элементом одежды и комплектом
    @Delete
    suspend fun delete(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef)

    // Получение всех элементов одежды для конкретного комплекта
    @Query("SELECT * FROM ClothingItem INNER JOIN ClothingItemOutfitCrossRef ON ClothingItem.id = ClothingItemOutfitCrossRef.clothingItemId WHERE ClothingItemOutfitCrossRef.outfitId = :outfitId")
    fun getClothingItemsForOutfit(outfitId: Int): LiveData<List<ClothingItem>>

    // Получение всех комплектов для конкретного элемента одежды
    @Query("SELECT * FROM Outfit INNER JOIN ClothingItemOutfitCrossRef ON Outfit.id = ClothingItemOutfitCrossRef.outfitId WHERE ClothingItemOutfitCrossRef.clothingItemId = :clothingItemId")
    fun getOutfitsForClothingItem(clothingItemId: Int): LiveData<List<Outfit>>

    //Удаление всязи для конкретного комплекта
    @Query("DELETE FROM ClothingItemOutfitCrossRef WHERE outfitId = :outfitId")
    fun deleteCrossRefsForOutfit(outfitId: Int)

    @Query("DELETE FROM ClothingItemOutfitCrossRef WHERE clothingItemId = :clothingItemId")
    fun deleteCrossRefsForClothingItem(clothingItemId: Int)
}
