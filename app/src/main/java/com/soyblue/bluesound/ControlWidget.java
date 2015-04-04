package com.soyblue.bluesound;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class ControlWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Create an Intent to launch ExampleActivity
        //Intent intent = BlueService.getToggleIntent(context);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //
        Intent intent = new Intent(context, BlueToggleReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 234324243, intent, 0);

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.control_widget);
        //Check if ON or if OFF
        if( BluetoothManager.getInstance().getStatus() ){
            views.setInt(R.id.widget_master, "setBackgroundResource",R.drawable.ic_audio_off );
        }else{
            views.setInt(R.id.widget_master, "setBackgroundResource",R.drawable.ic_audio_on );
        }

        views.setOnClickPendingIntent(R.id.widget_master, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if( AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction()) ){
            //Request update of all views
            int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ControlWidget.class));
            final int N = ids.length;
            for (int i = 0; i < N; i++) {
                updateAppWidget(context, AppWidgetManager.getInstance(context), ids[i]);
            }
        }
    }

    public static void requestWidgetUpdate(Context context ){
        Intent brIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.getApplicationContext().sendBroadcast(brIntent);

    }
}

