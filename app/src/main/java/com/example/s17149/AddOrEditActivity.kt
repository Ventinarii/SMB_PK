package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.s17149.DataBase.Shop
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityAddOrEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddOrEditActivity : AppCompatActivity() {

    private lateinit var biding: ActivityAddOrEditBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityAddOrEditBinding.inflate(layoutInflater);
        setContentView(biding.root);
    }
    fun buAcDelete(view: android.view.View) {
        if(AppLogic.shop!=null){
            val name = AppLogic.shop.name;
            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.delete(AppLogic.shop) }
            Toast.makeText(this,"deleted: "+name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }
    fun buAcSave(view: android.view.View) {
        if(AppLogic.shop!=null){
            AppLogic.shop.name = biding.nameTextField.text.toString();
            AppLogic.shop.qty = biding.qtyTextField.text.toString().toFloat();
            AppLogic.shop.price = biding.priceTextField.text.toString().toFloat();

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.update(AppLogic.shop) }
            Toast.makeText(this,"edited: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.shop = Shop(
                0,
                biding.nameTextField.text.toString(),
                biding.qtyTextField.text.toString().toFloat(),
                biding.priceTextField.text.toString().toFloat(),
                false
            );

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.insert(AppLogic.shop) }
            Toast.makeText(this,"added: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }
    override fun onResume() {
        super.onResume();
        if(AppLogic.shop!=null){
            biding.nameTextField.setText(AppLogic.shop.name);
            biding.qtyTextField.setText(AppLogic.shop.qty.toString());
            biding.priceTextField.setText(AppLogic.shop.price.toString());
        }else{
            biding.nameTextField.setText("");
            biding.qtyTextField.setText("");
            biding.priceTextField.setText("");
        }
    }

}