package com.example.s17149.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(app: Application):AndroidViewModel(app) {

    private val repo: ProductRepo;
    public val allProducts: LiveData<List<Product>>;

    init{
        val db = MyRoomDatabase.getDatabase(app);
        val productDao = db.productDao()
        repo = ProductRepo(productDao);
        allProducts = repo.allProducts;
    }

    suspend fun insert(product: Product) = repo.insert(product);

    suspend fun delete(product: Product) = repo.delete(product);

    suspend fun update(product: Product) = repo.update(product);

    suspend fun deleteAll() = repo.deleteAll();

}