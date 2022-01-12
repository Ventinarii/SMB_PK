package com.example.s17149

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.s17149.Adapters.ShopAdapter
import com.example.s17149.DataBase.ShopViewModel
import com.example.s17149.Logic.AppLogic
import com.example.s17149.Logic.WigetLogic
import com.example.s17149.R

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
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    WigetLogic.init(context.applicationContext);

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
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.m_p5);
    views.setTextViewText(R.id.textView, widgetText);
    views.setImageViewResource(R.id.imageView,R.drawable.wallpaer);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}