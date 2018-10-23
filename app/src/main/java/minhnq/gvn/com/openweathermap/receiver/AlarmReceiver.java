package minhnq.gvn.com.openweathermap.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import minhnq.gvn.com.openweathermap.service.NotificationService;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent !=null) {

            Log.d("in receiver", "onReceiver: ");
            String name = intent.getStringExtra(MainActivity.NAME);
            String main = intent.getStringExtra(MainActivity.MAIN);
            String temp = intent.getStringExtra(MainActivity.TEMP);
            Intent notifyIntent = new Intent(context, NotificationService.class);
            notifyIntent.putExtra(MainActivity.NAME, name);
            notifyIntent.putExtra(MainActivity.MAIN, main);
            notifyIntent.putExtra(MainActivity.TEMP, temp);
            context.startService(notifyIntent);

            // Set the Activity to start in a new, empty task
//            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            // Create the PendingIntent
//            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
//                    context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            mBuilder.setSmallIcon(R.drawable.icon_notify)
//                    .setContentTitle("Weather Now")
//                    .setContentText(name + ": " + main + " - " + temp + "°C");
//            mBuilder.setContentIntent(notifyPendingIntent);
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(001, mBuilder.build());
//            Log.d("Alarm","Alarms set for 1 minute.");

        }
    }
}
