package com.example.s17149.DataBase

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.s17149.Logic.AppLogic
import kotlin.coroutines.coroutineContext

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
        addGeoAlert(shop,context);
        repo.insert(shop);
    }

    suspend fun delete(shop: Shop,context: Context) {
        delGeoAlert(shop,context);
        repo.delete(shop);
    }

    suspend fun update(shopOld: Shop?,shopNew: Shop,context: Context) {
        if(shopOld!=null){
            delGeoAlert(shopOld,context);
            addGeoAlert(shopNew,context);
        }
        repo.update(shopNew);
    }

    suspend fun deleteAll(context: Context) {
        allShops.value?.forEach{e->delGeoAlert(e,context);};
        repo.deleteAll();
    }

    @SuppressLint("MissingPermission")
    suspend fun addGeoAlert(shop: Shop, context: Context){
        val intent = Intent().setComponent(
            ComponentName("com.example.s17149.Brodcast","com.example.s17149.Brodcast.GeoLocReceiver")
        ).also {
            it.putExtra("latitude", shop.latitude);
            it.putExtra("longtitude", shop.longtitude)
            it.putExtra("name", shop.name)
            it.putExtra("description", shop.description)
            it.putExtra("id", shop.id)
        }

        val pendingIntent = PendingIntent.getService(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        AppLogic.locationManager.addProximityAlert(
            AppLogic.shop.latitude,
            AppLogic.shop.longtitude,
            AppLogic.shop.radius.toFloat(),
            24*60*60*1000,
            pendingIntent)
    }
    suspend fun delGeoAlert(shop: Shop,context: Context){
        val intent = Intent().setComponent(
            ComponentName("com.example.s17149.Brodcast","com.example.s17149.Brodcast.GeoLocReceiver")
        ).also {
            it.putExtra("latitude", shop.latitude);
            it.putExtra("longtitude", shop.longtitude)
            it.putExtra("name", shop.name)
            it.putExtra("description", shop.description)
            it.putExtra("id", shop.id)
        }

        val pendingIntent = PendingIntent.getService(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        AppLogic.locationManager.removeProximityAlert(pendingIntent);
    }
}