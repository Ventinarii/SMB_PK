package com.example.s17149

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s17149.Adapters.ProductAdapter
import com.example.s17149.Adapters.ProductEditInterface
import com.example.s17149.DataBase.Product
import com.example.s17149.DataBase.ProductViewModel
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity(), ProductEditInterface{

    private lateinit var biding: ActivityProductListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityProductListBinding.inflate(layoutInflater);
        setContentView(biding.root);

        biding.rv1.layoutManager = LinearLayoutManager(this);
        biding.rv1.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        val viewModel = ProductViewModel(this.application);
        AppLogic.productViewModel = viewModel;
        val adapter = ProductAdapter(viewModel,this);
        viewModel.getProducts().observe(this, Observer {
            it.let {
                adapter.setProducts(it);
            }
        });

        biding.rv1.adapter = adapter;
    }

    /**
     * this button returns us to main activity
     */
    fun buAcProductListBack(view: android.view.View) {
        startActivity(AppLogic.mainActivity);
    }
    /**
     * currently abandoned
     */
    override fun onResume() {
        super.onResume();
        AppLogic.getAllVisibleProducts();
    }
    //============================================================================================== CODE
    /**
     * this button is responsible for adding product - it redirects us to AddOrEditActivity with AppLogic param
     * AppLogic.product == null
     */
    fun addProduct(view: android.view.View) {
        AppLogic.product = null;
        startActivity(AppLogic.addOrEditActivity);
    }

    /**
     * this function is part of interface ProductEditInterface and is responsible for receiving events coming from items in container
     * in this case the only function is to 'edit' otherwise define Enum and call function with it as additional argument (more functions  == mess)
     * @param product logical object associated with data_row that called event
     */
    override fun editProductOver(product: Product) {
        AppLogic.product = product;
        startActivity(AppLogic.addOrEditActivity);
    }
}