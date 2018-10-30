package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.adapter.LocationApdater;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.view.MainActivity;

public class UpdateService extends Service implements MainContract.IMainView, LocationApdater.ILocationListener {

    public static final String EXTRA_BUNDLE_FIVE_DAY = "bundleFiveDay";
    public static final String EXTRA_BUNDLE_ONE_DAY = "bundleOneDay";
    public static final String EXTRA_BUNDLE_SEARCH = "bundleSearch";
    public static final String EXTRA_LIST_SEARCH = "listSearch";
    public static final String ACTION_ONE_DAY = "oneDay";
    public static final String ACTION_FIVE_DAY = "fiveDay";
    public static final String ACITON_SEARCH = "search";
    private LocationApdater mLocationApdater;

    MainContract.IMainPresenter mIMainPresenter;
    private Weathers mWeather = new Weathers();
    Intent intent = new Intent();
//
//    public static void newInstance(Context context
//    ,
//                                   ,
//                                   ,
//                                   ,
//                                   ) {
//
//        Bundle args = new Bundle();
//
//
//        context.startService();
//    }

    protected MainContract.IMainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIMainPresenter = getPresenter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationApdater = new LocationApdater(this);
        mLocationApdater.initILocationListener(this);
        mIMainPresenter.getAllLocation(this);
        Bundle bundle = intent.getBundleExtra(MainActivity.EXTRA_BUNDLE_LOCATION);
        double longtitude = bundle.getDouble(MainActivity.EXTRA_LONGTITUDE);
        double latitude = bundle.getDouble(MainActivity.EXTRA_LATITUDE);
        getWeatherOneDay(latitude, longtitude);
        getWeatherFiveDay(latitude, longtitude);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(Weathers weather) {
        int temp = (int) weather.main.temp;
        //set data for widget

        intent.setAction(ACTION_ONE_DAY);
        Bundle bundleOneDay = new Bundle();
        bundleOneDay.putParcelable(MainActivity.EXTRA_WEATHER_ONE_DAY, weather);
        intent.putExtra(EXTRA_BUNDLE_ONE_DAY, bundleOneDay);
        sendBroadcast(intent);
        //   updateWidget();
    }

    @Override
    public void onResponeFiveDay(WeatherFiveDay weatherFiveDay) {
        Log.i("test", "onService: ");
        intent.setAction(ACTION_FIVE_DAY);
        Bundle bundleFiveDay = new Bundle();
        bundleFiveDay.putParcelable(MainActivity.EXTRA_WEATHER_FIVE_DAY, weatherFiveDay);
        intent.putExtra(EXTRA_BUNDLE_FIVE_DAY, bundleFiveDay);
        sendBroadcast(intent);
    }

    @Override
    public void onResponeAllLocation(List<Location> list) {
        Log.i("list", "onResponeAllLocation: " + String.valueOf(list.size()));
        intent.setAction(ACITON_SEARCH);
        Bundle bundleSearch = new Bundle();
        bundleSearch.putParcelableArrayList(EXTRA_LIST_SEARCH, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(EXTRA_BUNDLE_SEARCH, bundleSearch);
        sendBroadcast(intent);
    }


    @Override
    public void onClickLocation(Location location) {
        getWeatherOneDay(location.lattitude, location.longtitude);
    }

    private void getWeatherOneDay(double lat, double lon) {
        mIMainPresenter.getWeatherNow(String.valueOf(lat),
                String.valueOf(lon),
                Common.APP_ID, "metric");
    }

    private void getWeatherFiveDay(double lat, double lon) {

        mIMainPresenter.getWeatherFiveDay(String.valueOf(lat),
                String.valueOf(lon),
                5,
                Common.APP_ID, "metric");
    }
}
