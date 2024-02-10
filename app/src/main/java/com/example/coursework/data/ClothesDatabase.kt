package com.example.coursework.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coursework.data.dao.ClothesDao
import com.example.coursework.data.dao.ClothingItemOutfitCrossRefDao
import com.example.coursework.data.dao.OutfitDao
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit

@Database(
    entities = [ClothingItem::class, Outfit::class, ClothingItemOutfitCrossRef::class],
    version = 3,
    exportSchema = false
)

abstract class ClothesDatabase : RoomDatabase() {
    // Привязываем dao
    abstract fun clothesDao(): ClothesDao
    abstract fun outfitDao(): OutfitDao
    abstract fun clothesItemOutfitDao(): ClothingItemOutfitCrossRefDao

    companion object {
        // Переменная для хранения единственного экземпляра базы данных
        @Volatile
        private var INSTANCE: ClothesDatabase? = null

        // Функция для получения экземпляра базы данных
        fun getDatabase(context: Context): ClothesDatabase {
            // Проверяем, существует ли уже экземпляр базы данных
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // Синхронизируем доступ к созданию экземпляра базы данных, чтобы избежать возможных проблем с многопоточностью
            synchronized(this) {
                // Создаем экземпляр базы данных
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClothesDatabase::class.java,
                    "Clothes_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
