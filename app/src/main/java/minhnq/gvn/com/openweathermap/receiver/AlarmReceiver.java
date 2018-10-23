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
            String name = intent.getStringExtra(MainActivity.EXTRA_NAME);
            String main = intent.getStringExtra(MainActivity.EXTRA_MAIN);
            String temp = intent.getStringExtra(MainActivity.EXTRA_TEMP);
            Intent notifyIntent = new Intent(context, NotificationService.class);
            notifyIntent.putExtra(MainActivity.EXTRA_NAME, name);
            notifyIntent.putExtra(MainActivity.EXTRA_MAIN, main);
            notifyIntent.putExtra(MainActivity.EXTRA_TEMP, temp);
            context.startService(notifyIntent);


        }
    }
}
