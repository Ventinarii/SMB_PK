package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityAddOrEditBinding
import com.example.s17149.databinding.ActivityOptionsBinding

class AddOrEditActivity : AppCompatActivity() {

    private lateinit var biding: ActivityAddOrEditBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityAddOrEditBinding.inflate(layoutInflater);
        setContentView(biding.root);

    }

    fun buAcDelete(view: android.view.View) {

    }
    fun buAcSave(view: android.view.View) {

    }

    override fun onResume() {
        super.onResume();
        findAndUpdateUI();
    }
    fun findAndUpdateUI(){
        biding.ActivityTitle.setTextColor(AppLogic.textTrimColor.toArgb());
        biding.ActivityTitle.setBackgroundColor(AppLogic.trimColor.toArgb());

        biding.ScrollView.setBackgroundColor(AppLogic.mainColor.toArgb());
    }
}