package com.example.s17149.Adapters

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.util.Log
import com.example.s17149.R
import com.google.android.gms.location.Geofence


class GeoLocReceiver : BroadcastReceiver() {
    //https://stackoverflow.com/questions/15192347/android-how-to-set-a-proximity-alert-to-fire-only-when-exiting-or-only-when-ent
    //http://www.java2s.com/Code/Android/Core-Class/ProximityAlertDemo.htm
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        if (intent.data != null) Log.d("S17149PK_GeoLocReceiver_onReceive", intent.data.toString())
        val extras = intent.extras
        if (extras != null) {
            Log.d("S17149PK_GeoLocReceiver_onReceive", "Entering? " + (extras.getInt("com.google.android.location.intent.extra.transition")==(Geofence.GEOFENCE_TRANSITION_ENTER)))

            val latitude    = extras.getDouble("latitude",0.0);
            val longtitude  = extras.getDouble("longtitude",0.0);
            val name        = extras.getString("name","WTF");
            val description = extras.getString("description","WTF");
            val id          = extras.getLong("id",0);
            val enter       = (extras.getInt("com.google.android.location.intent.extra.transition")==(Geofence.GEOFENCE_TRANSITION_ENTER))

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