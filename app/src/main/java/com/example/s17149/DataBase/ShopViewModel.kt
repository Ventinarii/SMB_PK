package com.example.s17149.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ShopViewModel(app: Application):AndroidViewModel(app) {

    private val repo: ShopRepo;
    public val allProducts: LiveData<List<Shop>>;

    init{
        val db = MyRoomDatabase.getDatabase(app);
        val productDao = db.productDao()
        repo = ShopRepo(productDao);
        allProducts = repo.allProducts;
    }

    suspend fun insert(shop: Shop) = repo.insert(shop);

    suspend fun delete(shop: Shop) = repo.delete(shop);

    suspend fun update(shop: Shop) = repo.update(shop);

    suspend fun deleteAll() = repo.deleteAll();

}