package com.example.s17149

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
        startActivity(AppLogic.shopListActivity);
    }

    fun buAcSave(view: android.view.View) {
        if(AppLogic.shop!=null){
            AppLogic.shop.name = biding.nameTextField.text.toString();
            AppLogic.shop.description = biding.descriptionTextField.text.toString();
            AppLogic.shop.latitude = biding.latitudeTextField.text.toString().toLong();
            AppLogic.shop.longtitude = biding.longtitudeTextField.text.toString().toLong();
            AppLogic.shop.radius = biding.radiusTextField.text.toString().toLong();

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.update(AppLogic.shop) }
            Toast.makeText(this,"edited: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.shop = Shop(
                id = 0,
                name = biding.nameTextField.text.toString(),
                description = biding.descriptionTextField.text.toString(),
                latitude = biding.latitudeTextField.text.toString().toLong(),
                longtitude = biding.longtitudeTextField.text.toString().toLong(),
                radius = biding.radiusTextField.text.toString().toLong(),
                favorite = false
            );

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.insert(AppLogic.shop) }
            Toast.makeText(this,"added: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }
        startActivity(AppLogic.shopListActivity);
    }

    /**
     * what is happening here is we check if we got location in AppLogic (we EDIT object) or we rather
     * create NEW one. in latter case we take CURRENT location and insert it as default data.
     * we ALSO check permissions ONCE AGAIN.
     */
    override fun onResume() {
        super.onResume();
        var location: Location? = null;
        if(AppLogic.shop!=null){
            biding.nameTextField.setText(AppLogic.shop.name);
            biding.descriptionTextField.setText(AppLogic.shop.description);
            biding.latitudeTextField.setText(AppLogic.shop.latitude as String);
            biding.longtitudeTextField.setText(AppLogic.shop.longtitude as String);
            biding.radiusTextField.setText(AppLogic.shop.radius as String);
        }else{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),0);
            }
            location = AppLogic.locationManager.getLastKnownLocation(AppLogic.locationProvider);
            biding.nameTextField.setText("");
            biding.descriptionTextField.setText("");
            if(location!=null){
                biding.latitudeTextField.setText(location.latitude as String);
                biding.longtitudeTextField.setText(location.longitude as String);
            }else{
                biding.latitudeTextField.setText(0L as String);
                biding.longtitudeTextField.setText(0L as String);
            }
            biding.radiusTextField.setText(100L as String);
        }
    }

}