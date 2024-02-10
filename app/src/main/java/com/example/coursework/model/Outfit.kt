package com.example.coursework.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Outfit")
data class Outfit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val season: String,
    val style: String?,
    val description: String?,
    val dateUpdated: Long
)