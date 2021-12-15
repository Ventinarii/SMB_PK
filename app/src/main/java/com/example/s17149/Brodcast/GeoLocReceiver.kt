package com.example.s17149.Brodcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.location.LocationManager

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.s17149.Logic.AppLogic
import com.example.s17149.R


class GeoLocReceiver : BroadcastReceiver() {
    //https://stackoverflow.com/questions/15192347/android-how-to-set-a-proximity-alert-to-fire-only-when-exiting-or-only-when-ent
    //http://www.java2s.com/Code/Android/Core-Class/ProximityAlertDemo.htm
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        if (intent.data != null) Log.d("S17149PK_GeoLocReceiver_onReceive", intent.data.toString())
        val extras = intent.extras
        if (extras != null) {
            Log.d("S17149PK_GeoLocReceiver_onReceive", "Entering? " + extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING))

            val latitude    = extras.getDouble("latitude",0.0);
            val longtitude  = extras.getDouble("longtitude",0.0);
            val name        = extras.getString("name","WTF");
            val description = extras.getString("description","WTF");
            val id          = extras.getLong("id",0);
            val enter       = extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING)

            //create notification
            val notification = NotificationCompat
                .Builder(context,"channelId1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("ProximityAlert!")
                .setContentTitle("We noticed you ${if(enter) "enter" else "leave"} $name location with decription $description at $latitude / $longtitude")
                .setAutoCancel(true)
                .build();
            //channel
            val channel = NotificationChannel(
                "channelId1",
                "channelName1",
                NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManagerCompat.from(context)
                .createNotificationChannel(channel)
            //send notification
            NotificationManagerCompat
                .from(context)
                .notify(id.toInt(),notification);

        }else{
            Log.wtf("S17149PK_GeoLocReceiver_onReceive", "EMPTY INTENT")
        }

    }
}