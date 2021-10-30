package com.example.s17149.DataBase

import androidx.lifecycle.LiveData

class ProductRepo (private val productDao: ProductDao){

    val allProducts: LiveData<List<Product>> = productDao.getProducts();

    fun getProducts(): LiveData<List<Product>> = productDao.getProducts();

    suspend fun insert(product: Product) = productDao.insert(product);


    suspend fun delete(product: Product) = productDao.delete(product);


    suspend fun update(product: Product) = productDao.update(product);


    suspend fun deleteAll() = productDao.deleteAll();


}