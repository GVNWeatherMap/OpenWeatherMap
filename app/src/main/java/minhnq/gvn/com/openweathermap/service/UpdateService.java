package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Calendar;
import androidx.annotation.Nullable;
import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.widget.WeatherWidget;

public class UpdateService extends Service {
    private static final String TAG = "TAG";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RemoteViews remoteViews = buildUpdate(this);
        ComponentName widget = new ComponentName(this, WeatherWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(widget, remoteViews);
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    private RemoteViews buildUpdate(Context context) {
        Paper.init(context);
        int temp = Paper.book().read("temp");
        String city = Paper.book().read("city");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        String time = getCurrentDateTime();
        remoteViews.setTextViewText(R.id.tv_time_widget, time);
        remoteViews.setTextViewText(R.id.tv_city_widget, city);
        remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(temp) + "°");
        return remoteViews;
    }

    private String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour + ":" + (minute < 10 ? "0" : "") + minute;
    }
}
