package com.example.s17149.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.s17149.Logic.AppLogic

class ProductViewModel(app: Application):AndroidViewModel(app) {
    fun getProducts(): LiveData<List<Product>> = AppLogic.getAllProducts();
    fun insert(product: Product) = AppLogic.insert(product);
    fun delete(product: Product) = AppLogic.delete(product);
    fun update(product: Product) = AppLogic.update(product);
    fun deleteAll() = AppLogic.deleteAll();

}