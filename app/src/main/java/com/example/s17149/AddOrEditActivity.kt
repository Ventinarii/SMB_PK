package com.example.s17149

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.s17149.Brodcast.GeoLocReceiver
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

            val intent = Intent().setComponent(
                ComponentName("com.example.s17149.Brodcast","com.example.s17149.Brodcast.GeoLocReceiver")
            ).also {
                it.putExtra("latitude",AppLogic.shop.latitude);
                it.putExtra("longtitude",AppLogic.shop.longtitude)
                it.putExtra("name",AppLogic.shop.name)
                it.putExtra("description",AppLogic.shop.description)
                it.putExtra("id",AppLogic.shop.id)
            }
            val pendingIntent = PendingIntent.getService(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            AppLogic.locationManager.removeProximityAlert(pendingIntent);
        }
        startActivity(AppLogic.shopListActivity);
    }

    @SuppressLint("MissingPermission")
    fun buAcSave(view: android.view.View) {
        if(AppLogic.shop!=null){
            //delete old alert
            val intent = Intent().setComponent(
                ComponentName("com.example.s17149.Brodcast","com.example.s17149.Brodcast.GeoLocReceiver")).also {
                it.putExtra("latitude",AppLogic.shop.latitude);
                it.putExtra("longtitude",AppLogic.shop.longtitude)
                it.putExtra("name",AppLogic.shop.name)
                it.putExtra("description",AppLogic.shop.description)
                it.putExtra("id",AppLogic.shop.id)
            }
            val pendingIntent = PendingIntent.getService(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            AppLogic.locationManager.removeProximityAlert(pendingIntent);

            //modify object
            AppLogic.shop.name = biding.nameTextField.text.toString();
            AppLogic.shop.description = biding.descriptionTextField.text.toString();
            AppLogic.shop.latitude = biding.latitudeTextField.text.toString().toDouble();
            AppLogic.shop.longtitude = biding.longtitudeTextField.text.toString().toDouble();
            AppLogic.shop.radius = biding.radiusTextField.text.toString().toDouble();

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.update(AppLogic.shop) }
            Toast.makeText(this,"edited: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }else{
            AppLogic.shop = Shop(
                id = 0,
                name = biding.nameTextField.text.toString(),
                description = biding.descriptionTextField.text.toString(),
                latitude = biding.latitudeTextField.text.toString().toDouble(),
                longtitude = biding.longtitudeTextField.text.toString().toDouble(),
                radius = biding.radiusTextField.text.toString().toDouble(),
                favorite = false
            );

            CoroutineScope(Dispatchers.IO).launch { AppLogic.shopViewModel.insert(AppLogic.shop) }
            Toast.makeText(this,"added: "+AppLogic.shop.name, Toast.LENGTH_SHORT).show();
        }
        //add new alert
        val intent = Intent().setComponent(
            ComponentName("com.example.s17149.Brodcast","com.example.s17149.Brodcast.GeoLocReceiver")).also {
            it.putExtra("latitude",AppLogic.shop.latitude);
            it.putExtra("longtitude",AppLogic.shop.longtitude)
            it.putExtra("name",AppLogic.shop.name)
            it.putExtra("description",AppLogic.shop.description)
            it.putExtra("id",AppLogic.shop.id)
        }
        val pendingIntent = PendingIntent.getService(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        AppLogic.locationManager.addProximityAlert(
            AppLogic.shop.latitude,
            AppLogic.shop.longtitude,
            AppLogic.shop.radius.toFloat(),
            24*60*60*1000,
            pendingIntent)

        startActivity(AppLogic.shopListActivity);
    }

    /**
     * what is happening here is we check if we got location in AppLogic (we EDIT object) or we rather
     * create NEW one. in latter case we take CURRENT location and insert it as default data.
     * we ALSO check permissions ONCE AGAIN.
     */
    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume();
        var location: Location? = null;
        if(AppLogic.shop!=null){
            biding.nameTextField.setText(AppLogic.shop.name);
            biding.descriptionTextField.setText(AppLogic.shop.description);
            biding.latitudeTextField.setText(AppLogic.shop.latitude.toString());
            biding.longtitudeTextField.setText(AppLogic.shop.longtitude.toString());
            biding.radiusTextField.setText(AppLogic.shop.radius.toString());
        }else{
            location = AppLogic.locationManager.getLastKnownLocation(AppLogic.locationProvider);
            biding.nameTextField.setText("");
            biding.descriptionTextField.setText("");
            if(location!=null){
                biding.latitudeTextField.setText((location.latitude.toString()));
                biding.longtitudeTextField.setText((location.longitude.toString()));
            }else{
                biding.latitudeTextField.setText((0L).toString());
                biding.longtitudeTextField.setText((0L).toString());
            }
            biding.radiusTextField.setText((5000L).toString());
        }
    }

}