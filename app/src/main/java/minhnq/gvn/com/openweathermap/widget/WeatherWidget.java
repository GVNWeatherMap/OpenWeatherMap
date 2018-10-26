package minhnq.gvn.com.openweathermap.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import minhnq.gvn.com.openweathermap.service.UpdateService;

public class WeatherWidget extends AppWidgetProvider {
    private static final String TAG = "TAG";
    private static final String WIDGET_CLICK = "widget.click";
    private static final String CLOCK_WIDGET_UPDATE = "minhnq.gvn.com.openweathermap.widget.CLOCK_WIDGET_UPDATE";
    PendingIntent pendingIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate: ");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent(context, UpdateService.class);

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        context.startService(new Intent(context, UpdateService.class));
//        final int count = appWidgetIds.length;
//        AppWidgetManager widgetManager = appWidgetManager;
////        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);
//        for (int i = 0; i < count; i++) {
//            int widgetId = appWidgetIds[i];
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            Intent intent = new Intent(context, WeatherWidget.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, pendingIntent);
//            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, getPendingSelfIntent(context, TEMP_CLICK));
//            remoteViews.setOnClickPendingIntent(R.id.tv_city_widget, getPendingSelfIntent(context, CITY_CLICK));
//            remoteViews.setOnClickPendingIntent(R.id.rlv_widget, getPendingSelfIntent(context, WIDGET_CLICK));
//            remoteViews.setOnClickPendingIntent(R.id.tv_time_widget, getPendingSelfIntent(context, CLOCK_WIDGET_UPDATE));
//            widgetManager.updateAppWidget(widgetId, remoteViews);
//        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private PendingIntent createUpdateIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

//    @Override
//    public void onEnabled(Context context) {
//        super.onEnabled(context);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 50);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, createUpdateIntent(context));
//    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createUpdateIntent(context));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//        Paper.init(context);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
//        StrictMode.setThreadPolicy(policy);
//        boolean isInternetConnected = NetWorkStatus.isInternetConnected();
//
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//
//        // Check the internet connection availability
//        if (isInternetConnected) {
//            // Update the widget weather data
//            int temp = Paper.book().read("temp");
//            String city = Paper.book().read("city");
//            int minute = c.get(Calendar.MINUTE);
//            remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(temp) + "°");
//            remoteViews.setTextViewText(R.id.tv_city_widget, city);
//            remoteViews.setTextViewText(R.id.tv_time_widget, String.valueOf(c.get(Calendar.HOUR_OF_DAY) + ":" + (minute < 10 ? "0" : "") + String.valueOf(minute)));
//        } else {
//            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
//        }
//
//        // If the temperature text clicked
//        if (TEMP_CLICK.equals(intent.getAction())) {
//            Log.d(TAG, "onReceive: Temp click ");
//        }
//
//        if (CITY_CLICK.equals(intent.getAction())) {
//            Log.d(TAG, "onReceive: City click ");
//            // Do something
//        }
//
//        if (WIDGET_CLICK.equals(intent.getAction())) {
//            Intent intentMain = new Intent(context, MainActivity.class);
//            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intentMain);
//        }
        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            Intent intentService = new Intent(context, UpdateService.class);
            context.startService(intentService);
        }
        // Apply the changes
//        appWidgetManager.updateAppWidget(watchWidget, remoteViews);


    }
}
