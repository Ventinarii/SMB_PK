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

    /**
     * this button is fulfilling 2 functions:
     * 1) if not editing object (AppLogic.product==null) then delete proposed object and go back
     * 2) if editing object then DELETE IT and THEN go back
     */
    fun buAcDelete(view: android.view.View) {
        if(AppLogic.product!=null){
            val name = AppLogic.product.Name;
            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.delete(AppLogic.product) }
            Toast.makeText(this, "deleted: $name", Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.productListActivity);
    }

    /**
     * this button is fulfilling 2 fuctions:
     * 1) if not editing object (AppLogic.product==null) then CREATE IT and then go back
     * 2) if editing object then EDIT IT and then go back
     */
    fun buAcSave(view: android.view.View) {
        if(AppLogic.product!=null){
            AppLogic.product.Name = biding.nameTextField.text.toString();
            AppLogic.product.Qty = biding.qtyTextField.text.toString().toFloat();
            AppLogic.product.Price = biding.priceTextField.text.toString().toFloat();
            AppLogic.product.Public = biding.publicSwich.isChecked;

            CoroutineScope(Dispatchers.IO).launch { AppLogic.productViewModel.update(AppLogic.product) }
            Toast.makeText(this,"edited: "+AppLogic.product.Name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.product = Product(
                Id = 0,
                UserId = AppLogic.fUser.uid,
                Public = biding.publicSwich.isChecked,
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

    /**
     * this function checks if we have AppLogic.product as parameter and changes fills in text fields if that is the case
     */
    override fun onResume() {
        super.onResume();
        if(AppLogic.product!=null){
            biding.nameTextField.setText(AppLogic.product.Name);
            biding.qtyTextField.setText(AppLogic.product.Qty.toString());
            biding.priceTextField.setText(AppLogic.product.Price.toString());
            biding.publicSwich.isChecked = biding.publicSwich.isChecked;
        }else{
            biding.nameTextField.setText("");
            biding.qtyTextField.setText("");
            biding.priceTextField.setText("");
        }
    }
}