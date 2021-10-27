package com.example.s17149

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var biding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(biding.root);

        AppLogic.mainActivity = this;
        AppLogic.sp = getPreferences(Context.MODE_PRIVATE);
        AppLogic.spEditor = AppLogic.sp.edit();
        AppLogic.onAppStart();
    }
    fun buAc01ProductList(view: android.view.View) {


    }
    fun buAcXSettings(view: android.view.View) {


    }
}