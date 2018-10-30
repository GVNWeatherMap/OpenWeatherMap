package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;

public class UpdateService extends Service implements MainContract.IMainView {
    private static final String TAG = "UpdateService";
    private static final String WEATHER_METRIC = "metric";
    private static final int COUNT_DAY = 5;
    private MainContract.IMainPresenter presenter;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        presenter = new MainPresenter(this);
        Paper.init(this);
        Double lat = Paper.book().read(Constants.LOCATION_LAT);
        Double lon = Paper.book().read(Constants.LOCATION_LON);
        presenter.getWeatherNow(String.valueOf(lat),
                String.valueOf(lon),
                Common.APP_ID, WEATHER_METRIC);

        presenter.getWeatherFiveDay(String.valueOf(lat),
                String.valueOf(lon),
                COUNT_DAY,
                Common.APP_ID, WEATHER_METRIC);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onResponse(Weathers weather) {
        int temp = (int) weather.main.temp;
        Intent intent = new Intent(Constants.ACTION_WEATHER_ONEDAY);
        intent.putExtra(Constants.EXTRA_TEMP, temp);
        intent.putExtra(Constants.EXTRA_STT, weather.weather.get(0).main);
        intent.putExtra(Constants.EXTRA_CITY, weather.name);
        intent.putExtra(Constants.EXTRA_STATUS, weather.weather.get(0).description);
        sendBroadcast(intent);
    }

    @Override
    public void onResponseFiveDay(WeatherFiveDay weatherFiveDay) {
        Intent intent = new Intent(Constants.ACTION_WEATHER_FIVEDAY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_WEATHER_FIVEDAY, weatherFiveDay);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
}
