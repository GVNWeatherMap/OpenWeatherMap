package minhnq.gvn.com.openweathermap.service;


import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;

import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;
import minhnq.gvn.com.openweathermap.widget.WeatherWidget;

public class UpdateService extends Service implements MainContract.IMainView {
    private static final String TAG = "UpdateService";
    private static final int COUNT_DAY = 5;
    private static final String WEATHER_METRIC = "metric";
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
        intent.putExtra(Constants.EXTRA_CITY, weather.name);
        intent.putExtra(Constants.EXTRA_STATUS, weather.weather.get(0).description);
        sendBroadcast(intent);

//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);
//        String time = getCurrentDateTime();
//        remoteViews.setTextViewText(R.id.tv_time_widget, time);
//        remoteViews.setTextViewText(R.id.tv_city_widget, weather.name);
//        remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(temp) + "°");
//        ComponentName widget = new ComponentName(this, WeatherWidget.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        manager.updateAppWidget(widget, remoteViews);
    }

    @Override
    public void onResponseFiveDay(WeatherFiveDay weatherFiveDay) {
        Intent intent = new Intent(Constants.ACTION_WEATHER_FIVEDAY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_WEATHER_FIVEDAY, weatherFiveDay);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    //    private RemoteViews buildUpdate(Context context) {
//        Paper.init(context);
//        int temp = Paper.book().read("temp");
//        String city = Paper.book().read("city");
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        String time = getCurrentDateTime();
//        remoteViews.setTextViewText(R.id.tv_time_widget, time);
//        remoteViews.setTextViewText(R.id.tv_city_widget, city);
//        remoteViews.setTextViewText(R.id.tv_temperature, String.valueOf(temp) + "°");
//        return remoteViews;
//    }
}
