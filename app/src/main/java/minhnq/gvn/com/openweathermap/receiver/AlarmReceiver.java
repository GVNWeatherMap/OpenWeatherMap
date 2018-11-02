package minhnq.gvn.com.openweathermap.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.utils.Constants;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Constants.ACTION_WEATHER_NOTIFY)) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            NotificationManager mNotificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Bundle bundle = intent.getExtras();
            Weathers weathers = bundle.getParcelable(Constants.EXTRA_WEATHER_ONEDAY);
            Intent mainIntent = new Intent(context,MainActivity.class);
            // Set the Activity to start in a new, empty task
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setSmallIcon(R.drawable.icon_notify)
                    .setContentTitle("Weather Now")
                    .setAutoCancel(true)
                    .setContentText(weathers.name + ": " + weathers.weather.get(0).main + " - " + weathers.main.temp + "°C");
            mBuilder.setContentIntent(notifyPendingIntent);
            mNotificationManager.notify(001, mBuilder.build());
        }
    }
}
