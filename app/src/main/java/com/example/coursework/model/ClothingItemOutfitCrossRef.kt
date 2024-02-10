package com.example.coursework.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "ClothingItemOutfitCrossRef",
    primaryKeys = ["clothingItemId", "outfitId"],
    foreignKeys = [
        ForeignKey(entity = ClothingItem::class, parentColumns = ["id"], childColumns = ["clothingItemId"]),
        ForeignKey(entity = Outfit::class, parentColumns = ["id"], childColumns = ["outfitId"])
    ]
)
data class ClothingItemOutfitCrossRef(
    val clothingItemId: Int,
    val outfitId: Int
)
