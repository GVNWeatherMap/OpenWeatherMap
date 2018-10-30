package minhnq.gvn.com.openweathermap.presenter;


import android.content.Context;

import java.util.List;

import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.database.LocationSQLiteUtils;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.utils.APIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends BasePresenter<MainContract.IMainView> implements MainContract.IMainPresenter {

    public MainPresenter(MainContract.IMainView iMainView) {
        super(iMainView);
    }

    @Override
    public void getWeatherNow(String lat, String lng, String appid, String unit) {
        APIUtils.getAPIService().getWeatherByLatLng(lat, lng, appid, unit).enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                view.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Weathers> call, Throwable t) {

            }
        });
    }

    @Override
    public void getWeatherFiveDay(String lat, String lng, int cnt, String appid, String unit) {
        APIUtils.getAPIService().getWeatherFiveDayByLatLng(lat, lng, cnt, appid, unit).enqueue(new Callback<WeatherFiveDay>() {
            @Override
            public void onResponse(Call<WeatherFiveDay> call, Response<WeatherFiveDay> response) {
                view.onResponseFiveDay(response.body());
            }

            @Override
            public void onFailure(Call<WeatherFiveDay> call, Throwable t) {

            }

        });
    }

    @Override
    public void getAllLocation(Context context) {
        LocationSQLiteUtils sqLiteUtils = new LocationSQLiteUtils(context);
        sqLiteUtils.open();
        sqLiteUtils.dumpData();
        List<Location> list = sqLiteUtils.getAllLocation();
        sqLiteUtils.close();
//        view.onResponeAllLocation(list);
    }
}
