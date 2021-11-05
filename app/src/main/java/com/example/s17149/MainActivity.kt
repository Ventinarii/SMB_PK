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
    fun buAcXSettings(view: android.view.View) {
        startActivity(AppLogic.optionsActivity);
    }

    override fun onResume() {
        super.onResume();
        findAndUpdateUI();
    }

    fun findAndUpdateUI(){
        biding.titleTextView.setTextColor(AppLogic.textTrimColor.toArgb());
        biding.titleTextView.setBackgroundColor(AppLogic.trimColor.toArgb());

        biding.ScrollView.setBackgroundColor(AppLogic.mainColor.toArgb());

        biding.ProductListButton.setTextColor(AppLogic.textMainColor.toArgb());
        biding.SettingsButton.setTextColor(AppLogic.textMainColor.toArgb());

        biding.ProductListButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,AppLogic.mainTextSize.toFloat());
        biding.SettingsButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,AppLogic.mainTextSize.toFloat());
    }
}