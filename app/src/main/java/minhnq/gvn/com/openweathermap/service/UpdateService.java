package minhnq.gvn.com.openweathermap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;

public class UpdateService extends Service implements MainContract.IMainView {

    private Weathers mWeather = new Weathers();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(Weathers weather) {
        mWeather = weather;
    }

    @Override
    public void onResponeFiveDay(WeatherFiveDay weatherFiveDay) {

    }

    @Override
    public void onResponeAllLocation(List<Location> list) {

    }
}
