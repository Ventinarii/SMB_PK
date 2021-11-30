package com.example.s17149.DataBase

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [Product::class], version = 1)
@Deprecated("swich to firebase")
abstract class MyRoomDatabase: RoomDatabase() {

    abstract fun productDao():ProductDao

    companion object{
        private var instance: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase{
            if(instance!=null)
                return instance!!

            instance = Room.databaseBuilder(
                context.applicationContext,
                MyRoomDatabase::class.java,
                "ProductDatabase"
            ).build();

            return instance!!
        }
    }
}