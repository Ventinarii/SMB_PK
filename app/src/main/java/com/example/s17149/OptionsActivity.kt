package com.example.s17149

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.ActivityMainBinding
import com.example.s17149.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity() {

    private lateinit var biding: ActivityOptionsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        biding = ActivityOptionsBinding.inflate(layoutInflater);
        setContentView(biding.root);

        AppLogic.optionsActivity = this;
    }
}