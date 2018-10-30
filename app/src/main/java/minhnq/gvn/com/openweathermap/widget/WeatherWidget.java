package minhnq.gvn.com.openweathermap.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.receiver.UpdateTimeBroadcastReceiver;
import minhnq.gvn.com.openweathermap.utils.NetWorkStatus;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class WeatherWidget extends AppWidgetProvider {
    private static final String TAG = "TAG";
    private static final String TEMP_CLICK = "temp.click";
    private static final String CITY_CLICK = "city.click";
    private static final String WIDGET_CLICK = "widget.click";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i(TAG, "onUpdate: From update widget");
        final int count = appWidgetIds.length;
        AppWidgetManager widgetManager = appWidgetManager;
//        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent intent = new Intent(context, WeatherWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intentActivity = new Intent(context,MainActivity.class);
            intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntentActivity = PendingIntent.getActivity(context,0,intentActivity,0);
            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, pIntentActivity);
            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, getPendingSelfIntent(context, TEMP_CLICK));
            remoteViews.setOnClickPendingIntent(R.id.tv_city_widget, getPendingSelfIntent(context, CITY_CLICK));
            remoteViews.setOnClickPendingIntent(R.id.rlv_widget, getPendingSelfIntent(context, WIDGET_CLICK));
//            remoteViews.setTextViewText(R.id.txv_widget_hour,getCurrentTime("hh:mm"));
            Log.i(TAG, getCurrentTime("hh:mm:ss a"));
            widgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive: From widget");
        Paper.init(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        boolean isInternetConnected = NetWorkStatus.isInternetConnected();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);

        // Check the internet connection availability
        if (isInternetConnected) {
            // Update the widget weather data
            int temp = Paper.book().read("temp");
//            int temp = (int) number;
            String city = Paper.book().read("city");
            remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(temp) + "°");
            remoteViews.setTextViewText(R.id.tv_city_widget, city);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }

        // If the temperature text clicked
        if (TEMP_CLICK.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: Temp click ");
        }

        if (CITY_CLICK.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: City click ");
            Intent intentActivity = new Intent(context,MainActivity.class);
            PendingIntent pIntentActivity = PendingIntent.getActivity(context,0,intentActivity,0);
            // Do something
        }

        if (WIDGET_CLICK.equals(intent.getAction())) {
        }

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, UpdateTimeBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent1, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+ 100 * 3, 60000 , pi);
        // Apply the changes
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, UpdateTimeBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(pendingIntent);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }
    public static String getCurrentTime(String timeformat){
          Format formatter = new SimpleDateFormat(timeformat);
             return formatter.format(new Date());
         }
}
