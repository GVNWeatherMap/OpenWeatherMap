package minhnq.gvn.com.openweathermap.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.widget.WeatherWidget;

public class UpdateTimeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        //Acquire the lock
        wakeLock.acquire();
        Log.i("Update", "onUpdateTime " +getCurrentTime("hh:mm:ss a") );
           //You can do the processing here update the widget/remote views.
           RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                 R.layout.widget_layout);
           remoteViews.setTextViewText(R.id.tv_time_widget, getCurrentTime("hh:mm"));
           ComponentName thiswidget = new ComponentName(context, WeatherWidget.class);
           AppWidgetManager manager = AppWidgetManager.getInstance(context);
           manager.updateAppWidget(thiswidget, remoteViews);
           //Release the lock
        wakeLock.release();
    }
    public static String getCurrentTime(String timeformat){
        Format formatter = new SimpleDateFormat(timeformat);
        return formatter.format(new Date());
    }
}
