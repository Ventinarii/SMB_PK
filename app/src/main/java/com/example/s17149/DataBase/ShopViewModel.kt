package com.example.s17149.DataBase

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.GnssAntennaInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.s17149.Adapters.GeoLocReceiver
import com.example.s17149.Logic.AppLogic
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class ShopViewModel(app: Application):AndroidViewModel(app) {

    private val repo: ShopRepo;
    public val allShops: LiveData<List<Shop>>;

    init{
        val db = MyRoomDatabase.getDatabase(app);
        val productDao = db.productDao()
        repo = ShopRepo(productDao);
        allShops = repo.allProducts;
    }

    suspend fun insert(shop: Shop,context: Context) {
        addGeoAlert(shop,context,true);
        repo.insert(shop);
    }

    suspend fun delete(shop: Shop,context: Context) {
        delGeoAlert(shop,context);
        repo.delete(shop);
    }

    suspend fun update(shopOld: Shop?,shopNew: Shop,context: Context) {
        if(shopOld!=null){
            delGeoAlert(shopOld,context);
            addGeoAlert(shopNew,context,false);
        }
        repo.update(shopNew);
    }

    suspend fun deleteAll(context: Context) {
        allShops.value?.forEach{e->delGeoAlert(e,context);};
        repo.deleteAll();
    }

    private lateinit var geoClient:GeofencingClient;
    private lateinit var geoLocReceiver: GeoLocReceiver
    private var gId = 0;
    private var isNull = true
    private fun getGeoClient(context: Context):GeofencingClient{
        if(isNull){//do once
            geoClient = LocationServices.getGeofencingClient(context.applicationContext);


            geoLocReceiver = GeoLocReceiver();
            val intentFilter = IntentFilter();
            context.applicationContext.registerReceiver(geoLocReceiver,intentFilter);
            isNull=false;
        }
        return geoClient;
    }

    @SuppressLint("MissingPermission")
    private suspend fun addGeoAlert(shop: Shop, context: Context, calledFromInsert: Boolean) {
        getGeoClient(context);
        val intent = Intent(context.applicationContext,GeoLocReceiver::class.java)
        .also {
            it.putExtra("latitude", shop.latitude);
            it.putExtra("longtitude", shop.longtitude)
            it.putExtra("name", shop.name)
            it.putExtra("description", shop.description)
            it.putExtra("id", shop.id)
            it.putExtra("ENTER?",true)
        }
        var pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //---
        var geo = Geofence
            .Builder()
            .setRequestId("Geofence-${gId}")
            .setExpirationDuration(24*60*60*1000)
            .setCircularRegion(
                shop.latitude,
                shop.longtitude,
                shop.radius.toFloat())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)// or Geofence.GEOFENCE_TRANSITION_EXIT)
        var geoRequest = GeofencingRequest
            .Builder()
            .addGeofence(geo.build())
        if(calledFromInsert) geoRequest = geoRequest.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        var geoRequestBuild = geoRequest.build()
        //---
        geoClient.addGeofences(geoRequestBuild,pendingIntent)
            .addOnFailureListener {
                    e2-> Log.wtf("S17149PK_ShopViewModel_AddGeoAlert_2",e2.toString())
            }//.addOnSuccessListener(OnSuccessListener { Log.v("S17149PK_ShopViewModel_AddGeoAlert","OK1") })

        //for exiting===============================================================================
        intent.putExtra("ENTER?",false)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //---
        geo = geo
            .setRequestId("Geofence-${-gId}")
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
        geoRequest = GeofencingRequest
            .Builder()
            .addGeofence(geo.build())
        geoRequestBuild = geoRequest.build()
        //---
        geoClient.addGeofences(geoRequestBuild,pendingIntent)
            .addOnFailureListener {
                e4-> Log.wtf("S17149PK_ShopViewModel_AddGeoAlert_4",e4.toString())
            }//.addOnSuccessListener(OnSuccessListener { Log.v("S17149PK_ShopViewModel_AddGeoAlert","OK2") })
        //==========================================================================================
        gId++;
    }
    private suspend fun delGeoAlert(shop: Shop,context: Context){
        getGeoClient(context);
        val intent = Intent(context.applicationContext,GeoLocReceiver::class.java)
        .also {
            it.putExtra("latitude", shop.latitude);
            it.putExtra("longtitude", shop.longtitude)
            it.putExtra("name", shop.name)
            it.putExtra("description", shop.description)
            it.putExtra("id", shop.id)
            it.putExtra("ENTER?",true)
        }
        var pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        geoClient.removeGeofences(pendingIntent);
        //for exiting===============================================================================
        intent.putExtra("ENTER?",false)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        geoClient.removeGeofences(pendingIntent)
            .addOnFailureListener {
                e6-> Log.wtf("S17149PK_ShopViewModel_DelGeoAlert_6",e6.toString())
            }//.addOnSuccessListener(OnSuccessListener { Log.v("S17149PK_ShopViewModel_AddGeoAlert","OK3") })
    }
}