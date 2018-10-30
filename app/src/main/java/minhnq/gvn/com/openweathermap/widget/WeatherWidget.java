package minhnq.gvn.com.openweathermap.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.util.Calendar;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.service.UpdateService;
import minhnq.gvn.com.openweathermap.utils.Constants;
import minhnq.gvn.com.openweathermap.view.MainActivity;


public class WeatherWidget extends AppWidgetProvider {
    private static final String TAG = "TAG";
    private static final String WIDGET_CLICK = "widget.click";
    private static final String CLOCK_WIDGET_UPDATE = "minhnq.gvn.com.openweathermap.widget.CLOCK_WIDGET_UPDATE";
    private PendingIntent pendingIntent;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

//        AppWidgetManager widgetManager = appWidgetManager;
//        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);
//        for (int i = 0; i < appWidgetIds.length; i++) {
//            int widgetId = appWidgetIds[i];
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            Intent intentWidget = new Intent(context, WeatherWidget.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentWidget, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, pendingIntent);
//            remoteViews.setOnClickPendingIntent(R.id.rlv_widget, getPendingSelfIntent(context, WIDGET_CLICK));
//            widgetManager.updateAppWidget(widgetId, remoteViews);
//        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, UpdateService.class);
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        context.startService(new Intent(context, UpdateService.class));
    }

    private PendingIntent createUpdateIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createUpdateIntent(context));
        IntentFilter filter = new IntentFilter(Constants.ACTION_WEATHER_ONEDAY);
        context.registerReceiver(this, filter);
    }

    private String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour + ":" + (minute < 10 ? "0" : "") + minute;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (WIDGET_CLICK.equals(intent.getAction())) {
            Intent intentMain = new Intent(context, MainActivity.class);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMain);
        }

        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            Intent intentService = new Intent(context, UpdateService.class);
            context.startService(intentService);
        }

        if (Constants.ACTION_WEATHER_ONEDAY.equals(intent.getAction())) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            String time = getCurrentDateTime();
            remoteViews.setTextViewText(R.id.tv_time_widget, time);
            remoteViews.setTextViewText(R.id.tv_city_widget, intent.getStringExtra(Constants.EXTRA_CITY));
            remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(intent.getIntExtra(Constants.EXTRA_TEMP, 0)) + "°");
            ComponentName widget = new ComponentName(context, WeatherWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(widget, remoteViews);
        }
    }
}
