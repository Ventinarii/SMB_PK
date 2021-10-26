package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityOptionsBinding
import com.example.s17149.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {

    private lateinit var biding: ActivityProductListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityProductListBinding.inflate(layoutInflater);
        setContentView(biding.root);

        AppLogic.productListActivity = this;
    }
}