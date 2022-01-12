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

        val action = intent!!.action!!;
        when(action){
            "actionImage"->{
                WigetLogic.firstImage=!WigetLogic.firstImage;
            }
            "actionPlay"->{

            }
            "actionStart"->{

            }
            "actionPause"->{

            }
            "actionStop"->{

            }
            "actionNext"->{

            }
        }
        if(WigetLogic.appWidgetManager!=null&&WigetLogic.appWidgetId!=null)
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
    //views.setOnClickPendingIntent();
    //views.setOnClickPendingIntent();
    //views.setOnClickPendingIntent();

    // Instruct the widget manager to update the widget=============================================
    appWidgetManager.updateAppWidget(appWidgetId, views)
}