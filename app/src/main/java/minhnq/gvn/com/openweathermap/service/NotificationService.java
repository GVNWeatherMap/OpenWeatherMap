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
            String name = intent.getStringExtra(MainActivity.NAME);
            String main = intent.getStringExtra(MainActivity.MAIN);
            String temp = intent.getStringExtra(MainActivity.TEMP);
            Intent cancelIntent = new Intent();
            cancelIntent.setAction("CANCEL");
            // Set the Activity to start in a new, empty task
            cancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(
                    this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = mBuilder.build();
            mBuilder.setSmallIcon(R.drawable.icon_notify)
                    .setContentTitle("Weather Now")
                    .setOngoing(true)
                    .addAction(R.drawable.ic_launcher_background,"Cancel",notifyPendingIntent)
                    .setContentText(name + ": " + main + " - " + temp + "°C");
            mBuilder.setContentIntent(notifyPendingIntent);
            mNotificationManager.notify(001, mBuilder.build());
            startForeground(1,notification);
        }
        return START_NOT_STICKY;
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
