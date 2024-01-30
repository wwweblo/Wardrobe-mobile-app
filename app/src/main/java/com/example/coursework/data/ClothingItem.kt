package com.example.coursework.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Clothing_item")
data class ClothingItem (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String?,
    val title: String,
    val description: String,
    val season: String

)