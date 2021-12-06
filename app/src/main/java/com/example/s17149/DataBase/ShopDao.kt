package com.example.s17149.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDao {

    @Query("SELECT * FROM shop")
    fun getProducts():LiveData<List<Shop>>

    @Insert
    suspend fun insert(shop: Shop)

    @Delete
    suspend fun delete(shop: Shop)

    @Update
    suspend fun update(shop: Shop)

    @Query("DELETE FROM shop")
    suspend fun deleteAll()

}