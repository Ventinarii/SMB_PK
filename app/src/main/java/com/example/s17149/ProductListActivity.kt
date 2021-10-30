package com.example.s17149

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s17149.Adapters.ProductAdapter
import com.example.s17149.Adapters.ProductEditInterface
import com.example.s17149.DataBase.Product
import com.example.s17149.DataBase.ProductViewModel
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityOptionsBinding
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
        biding.rv1.adapter = adapter;


    }
    fun buAcProductListBack(view: android.view.View) {
        startActivity(AppLogic.mainActivity);
    }
    override fun onResume() {
        super.onResume();
        findAndUpdateUI();
        updateData();
        colorButton();
    }
    fun findAndUpdateUI(){
        biding.ActivityTitle.setTextColor(AppLogic.textTrimColor.toArgb());
        biding.ActivityTitle.setBackgroundColor(AppLogic.trimColor.toArgb());

        biding.ScrollView.setBackgroundColor(AppLogic.mainColor.toArgb());
    }

    //============================================================================================== CODE

    private var mode = 0;
    //0 - check toggle mode - green
    //1 - edit mode - yellow
    //2 - delete mode - red
    fun switchMode(view: android.view.View) {
        when(mode){
            0 -> mode = 1;
            1 -> mode = 2;
            2 -> mode = 0;
            else -> mode = 0;
        }
        colorButton();
    }
    fun colorButton(){
        when(mode){
            0 -> {biding.button.setBackgroundColor(Color.valueOf(0f,1f,0f).toArgb());biding.button.setTextColor(Color.valueOf(1f,1f,1f).toArgb())}
            1 -> {biding.button.setBackgroundColor(Color.valueOf(1f,1f,0f).toArgb());biding.button.setTextColor(Color.valueOf(0f,0f,0f).toArgb())}
            2 -> {biding.button.setBackgroundColor(Color.valueOf(1f,0f,0f).toArgb());biding.button.setTextColor(Color.valueOf(1f,1f,1f).toArgb())}
            else -> biding.button.setBackgroundColor(Color.valueOf(0f,0f,1f).toArgb());
        }
    }

    fun updateData(){
        //unnecessary
    }

    fun addProduct(view: android.view.View) {
        AppLogic.product = null;
        startActivity(AppLogic.addOrEditActivity);
    }
    override fun editProductOver(product: Product) {
        AppLogic.product = product;
        startActivity(AppLogic.addOrEditActivity);
    }

}