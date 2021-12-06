package com.example.s17149.DataBase

import androidx.lifecycle.LiveData

class ShopRepo (private val shopDao: ShopDao){

    val allProducts: LiveData<List<Shop>> = shopDao.getProducts();

    fun getProducts(): LiveData<List<Shop>> = shopDao.getProducts();

    suspend fun insert(shop: Shop) = shopDao.insert(shop);


    suspend fun delete(shop: Shop) = shopDao.delete(shop);


    suspend fun update(shop: Shop) = shopDao.update(shop);


    suspend fun deleteAll() = shopDao.deleteAll();


}