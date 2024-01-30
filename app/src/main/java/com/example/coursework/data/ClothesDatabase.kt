package com.example.coursework.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ClothingItem::class],
    version = 1,
    exportSchema = false
    )
abstract class ClothesDatabase: RoomDatabase() {

    abstract fun dao(): ClothesDao

    //Все, что пишется в companion object будет видно другим классам
    companion object{

        @Volatile
        private var INSTANCE: ClothesDatabase? = null

        fun getDatabase(context: Context): ClothesDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
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