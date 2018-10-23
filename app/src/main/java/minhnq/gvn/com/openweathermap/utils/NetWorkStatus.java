package minhnq.gvn.com.openweathermap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;

public class NetWorkStatus {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean isInternetConnected() {
        boolean status = false;
        try {
            InetAddress address = InetAddress.getByName("google.com");

            if (address != null) {
                status = true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }
}
