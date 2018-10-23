package minhnq.gvn.com.openweathermap.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class NotificationService extends Service {
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    NotificationManager mNotificationManager ;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent !=null){
            String name = intent.getStringExtra(MainActivity.EXTRA_NAME);
            String main = intent.getStringExtra(MainActivity.EXTRA_MAIN);
            String temp = intent.getStringExtra(MainActivity.EXTRA_TEMP);
            Intent mainIntent = new Intent(this,MainActivity.class);
            // Set the Activity to start in a new, empty task
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = mBuilder.build();
            mBuilder.setSmallIcon(R.drawable.icon_notify)
                    .setContentTitle("Weather Now")
                    .setContentText(name + ": " + main + " - " + temp + "°C");
            mBuilder.setContentIntent(notifyPendingIntent);
            mNotificationManager.notify(001, mBuilder.build());
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
