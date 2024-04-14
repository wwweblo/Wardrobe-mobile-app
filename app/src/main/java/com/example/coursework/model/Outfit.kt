package com.example.coursework.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Outfit")
data class Outfit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: String?,
    val title: String,
    val season: String,
    val style: String?,
    val description: String,
    val dateUpdated: Long
): Parcelable