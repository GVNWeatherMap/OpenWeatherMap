package minhnq.gvn.com.openweathermap.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import minhnq.gvn.com.openweathermap.service.NotificationService;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class CancelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        String action = intent.getAction();
        if(action.equals("CANCEL")){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.stopService(serviceIntent);

            Intent mainInten = new Intent(context, MainActivity.class);
            context.startActivity(mainInten);
        }
    }
}
