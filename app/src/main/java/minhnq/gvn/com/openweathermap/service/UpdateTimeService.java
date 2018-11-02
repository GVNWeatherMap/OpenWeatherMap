package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.widget.WeatherWidget;

public class UpdateTimeService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RemoteViews remoteView = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int minute = calendar.get(Calendar.MINUTE);
        remoteView.setTextViewText(R.id.tv_time_widget,String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
                + ":" +(minute < 10 ? "0" : "") +String.valueOf(minute));
        Log.i("time",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)  + ":" +(minute < 10 ? "0" : "") +String.valueOf(minute))
                + ":" +(minute < 10 ? "0" : "") +String.valueOf(minute) );
        ComponentName componentName = new ComponentName(this,WeatherWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(componentName,remoteView);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
