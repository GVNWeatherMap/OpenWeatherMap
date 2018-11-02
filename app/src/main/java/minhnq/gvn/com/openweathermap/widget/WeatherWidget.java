package minhnq.gvn.com.openweathermap.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.model.Weathers;
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
        Log.i(TAG, "onUpdate: ");
        AppWidgetManager widgetManager = appWidgetManager;
        ComponentName watchWidget = new ComponentName(context, WeatherWidget.class);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            Intent intentWidget = new Intent(context, WeatherWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentWidget, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.tv_temperature, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.rlv_widget, getPendingSelfIntent(context, WIDGET_CLICK));
            widgetManager.updateAppWidget(widgetId, remoteViews);
        }
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        final Intent intent = new Intent(context, UpdateTimeService.class);
//        if (pendingIntent == null) {
//            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);
//        context.startService(new Intent(context, UpdateTimeService.class));

//        context.startService(new Intent(context,UpdateTimeService.class));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND,1);
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),60000,getPendingIntent(context));
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

    }

    private String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour + ":" + (minute < 10 ? "0" : "") + minute ;
    }

    private PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(context,0 ,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
        Log.i(TAG, "onReceive: ");
        Paper.init(context);
        String cityName = Paper.book().read(Constants.EXTRA_CITY_NAME);
        double temp = Paper.book().read(Constants.EXTRA_TEMP);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        String time = getCurrentDateTime();
        remoteViews.setTextViewText(R.id.tv_time_widget, time);
        remoteViews.setTextViewText(R.id.tv_city_widget, cityName);
        remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf((int)temp) + "°");
        ComponentName widget = new ComponentName(context, WeatherWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(widget, remoteViews);
        if (WIDGET_CLICK.equals(intent.getAction())) {
            Intent intentMain = new Intent(context, MainActivity.class);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMain);

        }

        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            RemoteViews remoteViewsUpdate = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            String timeUpdate = getCurrentDateTime();
            remoteViewsUpdate.setTextViewText(R.id.tv_time_widget, timeUpdate);
            ComponentName widgetUpdate = new ComponentName(context, WeatherWidget.class);
            AppWidgetManager managerUpdate = AppWidgetManager.getInstance(context);
            managerUpdate.updateAppWidget(widgetUpdate, remoteViewsUpdate);
//            Intent intentService = new Intent(context, UpdateTimeService.class);
//            context.startService(intentService);
        }

        if (Constants.ACTION_WEATHER_ONEDAY_BY_NAME.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Weathers weathers = bundle.getParcelable(Constants.EXTRA_WEATHER_ONEDAY);
            String cityNameAccented = Paper.book().read(Constants.EXTRA_CITY_NAME_ACCENTED);
            RemoteViews remoteViewsUpdate = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            String timeUpdate = getCurrentDateTime();
            int tempUpdate = (int)weathers.main.temp;
            remoteViewsUpdate.setTextViewText(R.id.tv_time_widget, timeUpdate);
            remoteViewsUpdate.setTextViewText(R.id.tv_city_widget, cityNameAccented);
            remoteViewsUpdate.setTextViewText(R.id.tv_temperature, String.valueOf(tempUpdate) + "°");
            ComponentName widgetUpdate = new ComponentName(context, WeatherWidget.class);
            AppWidgetManager managerUpdate = AppWidgetManager.getInstance(context);
            managerUpdate.updateAppWidget(widgetUpdate, remoteViewsUpdate);
        }
    }
}
