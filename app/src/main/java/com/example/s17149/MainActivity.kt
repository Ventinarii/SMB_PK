package com.example.s17149

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
        AppLogic.shopListActivity = Intent(this,ShopListActivity::class.java);
        AppLogic.addOrEditActivity = Intent(this,AddOrEditActivity::class.java);
        AppLogic.mapActivity = Intent(this,MapsActivity::class.java);
        //AppLogic.optionsActivity = Intent(this,OptionsActivity::class.java);

        checkPermissions();
    }
    fun buAc01ShopList(view: android.view.View) {
        if(checkPermissions())startActivity(AppLogic.shopListActivity);
    }
    fun buAc02Map(view: android.view.View) {
        if(checkPermissions())startActivity(AppLogic.mapActivity);
    }
    override fun onResume() {
        super.onResume();
    }
    //============================================================================================== CODE
    /**
     * this function fills variables in AppLogic responsible for location & checks permissions for using those
     * IF vars & permissions are OK returns true - otherwise false.
     */
    @SuppressLint("MissingPermission")
    fun checkPermissions(): Boolean{
        AppLogic.locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        val crt = Criteria();
        crt.accuracy = Criteria.ACCURACY_FINE;
        AppLogic.locationProvider = AppLogic.locationManager.getBestProvider(crt,false);

        requestPermissions(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),0);
        doOnce();
        var location = AppLogic.locationManager.getLastKnownLocation(AppLogic.locationProvider);
        if(location!=null)Toast.makeText(this,"loc: ${location.latitude}, ${location.longitude}",Toast.LENGTH_LONG).show();
        return true;
    }

    private var done = false;
    private lateinit var locationListener: LocationListener
    /**
     * this func is called by checkPermissions and sets up listener for notifications. it will attempt to do so
     * 1) on app start
     * 2) before entering any other activity
     */
    @SuppressLint("MissingPermission")
    fun doOnce(){
        if(!done){
            done = true;
            locationListener = object: LocationListener{
                override fun onLocationChanged(p0: Location) {
                    Log.i("S17149PK_MainActivity_locationListener","loc: ${p0.latitude}, ${p0.longitude}");
                }
            }
            AppLogic.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000L,1F,locationListener);



        }
    }
}