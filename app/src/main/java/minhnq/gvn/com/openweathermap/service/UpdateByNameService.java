package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;

public class UpdateByNameService extends Service implements MainContract.IMainView {

    private static final String TAG = "UpdateByNameService";

    MainContract.IMainPresenter mPresenter;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPresenter = new MainPresenter(this);
        Bundle bundle = intent.getBundleExtra(Constants.EXTRA_BUNDLE_CITY_NAME);
        String cityName = bundle.getString(Constants.EXTRA_CITY_NAME);
        if (cityName != null) {
            getWeatherNowName(cityName);
            getWeatherFiveDayByName(cityName);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onResponse(Weathers weather) {

    }

    @Override
    public void onResponseFiveDay(WeatherFiveDay weatherFiveDay) {

    }

    @Override
    public void onGetAllLocation(List<Location> locationList) {

    }

    @Override
    public void onResponceNowByName(Weathers weathers) {
        Log.i(TAG, weathers.name);
        Intent intent = new Intent(Constants.ACTION_WEATHER_ONEDAY_BY_NAME);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_WEATHER_ONEDAY, weathers);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public void onResponseFiveDayByName(WeatherFiveDay weatherFiveDay) {
        Log.i(TAG, String.valueOf(weatherFiveDay.list.size()));
        Intent intent = new Intent(Constants.ACTION_WEATHER_FIVEDAY_BY_NAME);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_WEATHER_FIVEDAY, weatherFiveDay);

        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public void onResponseError(String error) {
        Intent intent = new Intent(Constants.ACTION_WEATHER_ERROR);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_WEATHER_ERROR,error);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private void getWeatherNowName(String name){
        mPresenter.getWeatherNowByName(name,
                Common.APP_ID,
                Constants.WEATHER_METRIC);
    }
    private void getWeatherFiveDayByName(String name){
        mPresenter.getWeatherFiveDayByName(name,
                Constants.COUNT_DAY,
                Common.APP_ID,
                Constants.WEATHER_METRIC);
    }
}
