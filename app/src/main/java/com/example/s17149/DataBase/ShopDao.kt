package com.example.s17149.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopDao {

    @Query("SELECT * FROM shop")
    fun getProducts():LiveData<List<Shop>>

    @Insert
    fun insert(shop: Shop)

    @Delete
    fun delete(shop: Shop)

    @Update
    fun update(shop: Shop)

    @Query("DELETE FROM shop")
    fun deleteAll()

}