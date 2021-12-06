package com.example.s17149.DataBase

import android.content.Context
import androidx.room.*

@Database(entities = [Shop::class], version = 1)
abstract class MyRoomDatabase: RoomDatabase() {

    abstract fun productDao():ShopDao

    companion object{
        private var instance: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase{
            if(instance!=null)
                return instance!!

            instance = Room.databaseBuilder(
                context.applicationContext,
                MyRoomDatabase::class.java,
                "ShopDatabase"
            ).build();

            return instance!!
        }
    }
}