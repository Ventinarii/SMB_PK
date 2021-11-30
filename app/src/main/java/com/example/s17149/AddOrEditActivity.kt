package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.s17149.DataBase.Product
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
        if(AppLogic.product!=null){
            val name = AppLogic.product.Name;
            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.delete(AppLogic.product) }
            Toast.makeText(this, "deleted: $name", Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }
    fun buAcSave(view: android.view.View) {
        if(AppLogic.product!=null){
            AppLogic.product.Name = biding.nameTextField.text.toString();
            AppLogic.product.Qty = biding.qtyTextField.text.toString().toFloat();
            AppLogic.product.Price = biding.priceTextField.text.toString().toFloat();

            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.update(AppLogic.product) }
            Toast.makeText(this,"edited: "+AppLogic.product.Name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.product = Product(
                Id =0,
                UserId = AppLogic.UserId,
                Public = AppLogic.UserIsPublishing,
                Name = biding.nameTextField.text.toString(),
                Qty = biding.qtyTextField.text.toString().toFloat(),
                Price = biding.priceTextField.text.toString().toFloat(),
                Click = false
            );

            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.insert(AppLogic.product) }
            Toast.makeText(this,"added: "+AppLogic.product.Name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }

    override fun onResume() {
        super.onResume();
        if(AppLogic.product!=null){
            biding.nameTextField.setText(AppLogic.product.Name);
            biding.qtyTextField.setText(AppLogic.product.Qty.toString());
            biding.priceTextField.setText(AppLogic.product.Price.toString());
        }else{
            biding.nameTextField.setText("");
            biding.qtyTextField.setText("");
            biding.priceTextField.setText("");
        }
    }
}