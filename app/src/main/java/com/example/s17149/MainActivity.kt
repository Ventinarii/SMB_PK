package com.example.s17149

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var biding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(biding.root);

        if(AppLogic.sp==null){
            AppLogic.sp = getPreferences(MODE_PRIVATE);
            AppLogic.spEditor = AppLogic.sp.edit();
        }

        AppLogic.onAppStart(this);

        AppLogic.mainActivity = Intent(this,MainActivity::class.java);
        AppLogic.productListActivity = Intent(this,ProductListActivity::class.java);
        AppLogic.addOrEditActivity = Intent(this,AddOrEditActivity::class.java);

        AppLogic.optionsActivity = Intent(this,OptionsActivity::class.java);
    }
    fun buAc01ProductList(view: android.view.View) {
        startActivity(AppLogic.productListActivity);
    }
    override fun onResume() {
        super.onResume();
    }
}