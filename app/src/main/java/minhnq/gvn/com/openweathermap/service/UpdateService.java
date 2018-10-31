package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.adapter.LocationApdater;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.Location;
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
    public static final String EXTRA_BUNDLE_FIVE_DAY = "bundleFiveDay";
    public static final String EXTRA_BUNDLE_ONE_DAY = "bundleOneDay";
    public static final String EXTRA_BUNDLE_SEARCH = "bundleSearch";
    public static final String EXTRA_LIST_SEARCH = "listSearch";
    private LocationApdater mLocationApdater;


    private Weathers mWeather = new Weathers();
    Intent intent = new Intent();
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
//        double lat = Paper.book().read(Constants.LOCATION_LAT);
//        double lon = Paper.book().read(Constants.LOCATION_LON);
        Bundle bundle = intent.getBundleExtra(Constants.EXTRA_BUNDLE_LOCATION);
        double lon = bundle.getDouble(Constants.EXTRA_LONG);
        double lat = bundle.getDouble(Constants.EXTRA_LAT);

        presenter.getWeatherNow(String.valueOf(lat),
                String.valueOf(lon),
                Common.APP_ID, WEATHER_METRIC);

        presenter.getWeatherFiveDay(String.valueOf(lat),
                String.valueOf(lon),
                COUNT_DAY,
                Common.APP_ID, WEATHER_METRIC);
        mLocationApdater = new LocationApdater(this);
        presenter.getAllLocation(this);

        return START_REDELIVER_INTENT;
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

    @Override
    public void onGetAllLocation(List<Location> locationList) {
        Log.i("list", "onResponeAllLocation: " + String.valueOf(locationList.size()));
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_WEATHER_SEARCH);
        Bundle bundleSearch = new Bundle();
        bundleSearch.putParcelableArrayList(EXTRA_LIST_SEARCH, (ArrayList<? extends Parcelable>) locationList);
        intent.putExtra(EXTRA_BUNDLE_SEARCH, bundleSearch);
        sendBroadcast(intent);
    }
}
