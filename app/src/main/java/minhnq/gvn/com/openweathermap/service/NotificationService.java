package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.List;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.constract.MainContract.IMainView;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;

public class NotificationService extends Service implements IMainView {
   MainPresenter notifyPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyPresenter = new MainPresenter(this);
        Bundle bundle = intent.getBundleExtra(Constants.EXTRA_BUNDLE_LOCATION);
        double lon = bundle.getDouble(Constants.EXTRA_LONG);
        double lat = bundle.getDouble(Constants.EXTRA_LAT);
        getWeatherNow(lat,lon);
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

    @Override
    public void onResponse(Weathers weather) {
        Intent intent = new Intent(Constants.ACTION_WEATHER_NOTIFY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_WEATHER_ONEDAY, weather);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public void onResponseFiveDay(WeatherFiveDay weatherFiveDay) {

    }

    @Override
    public void onGetAllLocation(List<Location> locationList) {

    }

    @Override
    public void onResponceNowByName(Weathers weathers) {

    }

    @Override
    public void onResponseFiveDayByName(WeatherFiveDay weatherFiveDay) {

    }

    @Override
    public void onResponseError(String error) {

    }

    private void getWeatherNow(double lat, double lon) {
        notifyPresenter.getWeatherNow(String.valueOf(lat),
                String.valueOf(lon),
                Common.APP_ID, UpdateService.WEATHER_METRIC);
    }
}
