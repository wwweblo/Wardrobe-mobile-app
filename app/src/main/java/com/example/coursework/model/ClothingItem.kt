package com.example.coursework.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Clothing_item")
data class ClothingItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String?,
    val title: String,
    val description: String,
    val season: String

): Parcelable