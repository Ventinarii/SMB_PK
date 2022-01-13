package com.example.s17149

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.example.s17149.Logic.WigetLogic
import android.content.ComponentName
import android.media.MediaPlayer
import android.util.Log


/**
 * Implementation of App Widget functionality.
 */
class MP5 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent);
        try {
            if (WigetLogic.playerStatus == WigetLogic.status.complete
            ) {//========================================================================================kill old player - we don't play replay here
                WigetLogic.player.stop();
                WigetLogic.playerStatus = WigetLogic.status.stopped;
                WigetLogic.player.release();
                WigetLogic.player = null;
                WigetLogic.playerStatus = WigetLogic.status.empty;
            }

            var action = intent?.action;
            if(action.isNullOrEmpty())
                action = "null";//skip when()
            when (action) {
                "actionImage" -> {
                    WigetLogic.firstImage = !WigetLogic.firstImage;
                }
                "actionPlay" -> {
                    if (WigetLogic.playerStatus == WigetLogic.status.stopped
                    ) {//================================================================================cleanup old player
                        WigetLogic.player.release();
                        WigetLogic.player = null;
                        WigetLogic.playerStatus = WigetLogic.status.empty;
                    }

                    if (WigetLogic.playerStatus == WigetLogic.status.empty
                    ) {//================================================================================create player if missing
                        WigetLogic.player = MediaPlayer.create(
                            context,
                            if (WigetLogic.firstSong) R.raw.red_army_choir_the_hunt_for_red_october else R.raw.flashback
                        )
                        WigetLogic.playerStatus = WigetLogic.status.preapared;

                        WigetLogic.player.setOnCompletionListener {//<==================================!!LISTENER!!
                            WigetLogic.playerStatus = WigetLogic.status.complete;
                        }
                    }

                    if (WigetLogic.playerStatus == WigetLogic.status.started
                    ) {//================================================================================pause IF running
                        WigetLogic.player.pause();
                        WigetLogic.playerStatus = WigetLogic.status.paused;
                    } else if (
                        WigetLogic.playerStatus == WigetLogic.status.preapared ||
                        WigetLogic.playerStatus == WigetLogic.status.paused
                    ) {//================================================================================ELSE^ start
                        WigetLogic.player.start();
                        WigetLogic.playerStatus = WigetLogic.status.started;
                    }
                }
                "actionStop" -> {
                    if (
                        WigetLogic.playerStatus == WigetLogic.status.started ||
                        WigetLogic.playerStatus == WigetLogic.status.paused ||
                        WigetLogic.playerStatus == WigetLogic.status.complete
                    ) {//================================================================================stop player and cleanup
                        WigetLogic.player.stop();
                        WigetLogic.playerStatus = WigetLogic.status.stopped;
                        WigetLogic.player.release();
                        WigetLogic.player = null;
                        WigetLogic.playerStatus = WigetLogic.status.empty;
                    }
                }
                "actionNext" -> {
                    if (WigetLogic.playerStatus != WigetLogic.status.empty
                    ) {//================================================================================stop player and cleanup
                        WigetLogic.player.stop();
                        WigetLogic.playerStatus = WigetLogic.status.stopped;
                        WigetLogic.player.release();
                        WigetLogic.player = null;
                        WigetLogic.playerStatus = WigetLogic.status.empty;
                    }
                    //==================================================================================switch songs
                    WigetLogic.firstSong = !WigetLogic.firstSong;
                    //==================================================================================create player if missing
                    WigetLogic.player = MediaPlayer.create(
                        context,
                        if (WigetLogic.firstSong) R.raw.red_army_choir_the_hunt_for_red_october else R.raw.flashback
                    )
                    WigetLogic.playerStatus = WigetLogic.status.preapared;

                    WigetLogic.player.setOnCompletionListener {//<======================================!!LISTENER!!
                        WigetLogic.playerStatus = WigetLogic.status.complete;
                    }
                    //==================================================================================start
                    WigetLogic.player.start();
                    WigetLogic.playerStatus = WigetLogic.status.started;
                }
            }
        }catch (ex:Exception){
            Log.wtf("s17149PLayer",ex.toString());
            ex.printStackTrace();
        }
        if(WigetLogic.appWidgetManager!=null)
            updateAppWidget(context!!.applicationContext,WigetLogic.appWidgetManager,WigetLogic.appWidgetId);
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    WigetLogic.appWidgetManager=appWidgetManager;
    WigetLogic.appWidgetId=appWidgetId;

    WigetLogic.init(context.applicationContext);

    //text
    var widgetText = WigetLogic
        .repo
        .allProducts
        .value
        ?.stream()
        ?.map { e->e.name+" "+e.description }
        ?.reduce { a, b->a+b }
        ?.get();
    if(widgetText.isNullOrEmpty())
        widgetText = "no shops for display";

    //browser
    val siteIntent = Intent(Intent.ACTION_VIEW);
    siteIntent.data = Uri.parse("https://www.pja.edu.pl")
    val pendingSiteIntent = PendingIntent.getActivity(
        context,
        0,
        siteIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    );

    //image
    val imageIntent = Intent(context.applicationContext,MP5::class.java);
    imageIntent.action = "actionImage"
    val pendingImageIntent = PendingIntent.getBroadcast(
        context,
        0,
        imageIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    );

    //player
    val playIntent = Intent(context.applicationContext,MP5::class.java);
    playIntent.action = "actionPlay"
    val pendingPlayIntent = PendingIntent.getBroadcast(
        context,
        0,
        playIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    );
    val stopIntent = Intent(context.applicationContext,MP5::class.java);
    stopIntent.action = "actionStop"
    val pendingStopIntent= PendingIntent.getBroadcast(
        context,
        0,
        stopIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    );
    val nextIntent = Intent(context.applicationContext,MP5::class.java);
    nextIntent.action = "actionNext"
    val pendingNextIntent = PendingIntent.getBroadcast(
        context,
        0,
        nextIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    );

    // Construct the RemoteViews object=============================================================
    val views = RemoteViews(context.packageName, R.layout.m_p5);
    //set text & image
    views.setTextViewText(R.id.textView, widgetText);
    views.setImageViewResource(R.id.imageView,
        if (WigetLogic.firstImage) R.drawable.wallper else R.drawable.space
    );

    //browser & image
    views.setOnClickPendingIntent(R.id.Browser,pendingSiteIntent);
    views.setOnClickPendingIntent(R.id.Image,pendingImageIntent);

    //player
    views.setOnClickPendingIntent(R.id.PausePlay,pendingPlayIntent);
    views.setOnClickPendingIntent(R.id.Stop,pendingStopIntent);
    views.setOnClickPendingIntent(R.id.Next,pendingNextIntent);

    // Instruct the widget manager to update the widget=============================================
    appWidgetManager.updateAppWidget(appWidgetId, views)
}