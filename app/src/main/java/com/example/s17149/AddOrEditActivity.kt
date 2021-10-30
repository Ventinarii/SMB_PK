package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.s17149.DataBase.Product
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityAddOrEditBinding
import com.example.s17149.databinding.ActivityOptionsBinding
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
        if(AppLogic.product!=null){
            val name = AppLogic.product.name;
            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.delete(AppLogic.product) }
            Toast.makeText(this,"deleted: "+name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }
    fun buAcSave(view: android.view.View) {
        if(AppLogic.product!=null){
            AppLogic.product.name = biding.nameTextField.text.toString();
            AppLogic.product.qty = biding.qtyTextField.text.toString().toFloat();
            AppLogic.product.price = biding.priceTextField.text.toString().toFloat();

            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.delete(AppLogic.product) }
            Toast.makeText(this,"edited: "+AppLogic.product.name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.product = Product(0,biding.nameTextField.text.toString(),biding.qtyTextField.text.toString().toFloat(),biding.priceTextField.text.toString().toFloat());

            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.delete(AppLogic.product) }
            Toast.makeText(this,"added: "+AppLogic.product.name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }

    override fun onResume() {
        super.onResume();
        findAndUpdateUI();

        if(AppLogic.product!=null){
            biding.nameTextField.setText(AppLogic.product.name);
            biding.qtyTextField.setText(AppLogic.product.qty.toString());
            biding.priceTextField.setText(AppLogic.product.price.toString());
        }else{
            biding.nameTextField.setText("");
            biding.qtyTextField.setText("");
            biding.priceTextField.setText("");
        }
    }
    fun findAndUpdateUI(){
        biding.ActivityTitle.setTextColor(AppLogic.textTrimColor.toArgb());
        biding.ActivityTitle.setBackgroundColor(AppLogic.trimColor.toArgb());

        biding.ScrollView.setBackgroundColor(AppLogic.mainColor.toArgb());
    }
}