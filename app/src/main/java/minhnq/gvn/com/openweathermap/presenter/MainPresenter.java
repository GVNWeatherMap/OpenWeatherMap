package minhnq.gvn.com.openweathermap.presenter;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.database.LocationOpenHelper;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.utils.APIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends BasePresenter<MainContract.IMainView> implements MainContract.IMainPresenter {
    LocationOpenHelper dbhelper;
    List<Location> list;
    public MainPresenter(MainContract.IMainView iMainView) {
        super(iMainView);
    }

    @Override
    public void getWeatherNow(String lat, String lng, String appid, String unit) {
        APIUtils.getAPIService().getWeatherByLatLng(lat, lng, appid, unit).enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                if(response.isSuccessful()){
                    view.onResponse(response.body());
                }else {
                    view.onResponseError("Not found!");
                }
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
                if(response.isSuccessful()){
                    view.onResponseFiveDay(response.body());
                }else {
                    view.onResponseError("Not found!");
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDay> call, Throwable t) {

            }

        });
    }

    @Override
    public void getAllLocation(Context context) {

        dbhelper = new LocationOpenHelper(context);
        try {
            dbhelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list = dbhelper.getDetails();
        view.onGetAllLocation(list);
    }

    @Override
    public void getWeatherNowByName(String city, String appid, String unit) {
        APIUtils.getAPIService().getWeatherByName(city,appid,unit).enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                    if(response.isSuccessful()){
                        view.onResponceNowByName(response.body());
                        Log.i("success", response.body().name);
                    }else {
                        view.onResponseError("Not found!");
                    }
            }

            @Override
            public void onFailure(Call<Weathers> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
        Log.i("error", "error");
    }

    @Override
    public void getWeatherFiveDayByName(String city,int cnt, String appid, String unit) {
        APIUtils.getAPIService().getWeatherFiveDayByName(city,cnt,appid,unit).enqueue(new Callback<WeatherFiveDay>() {
            @Override
            public void onResponse(Call<WeatherFiveDay> call, Response<WeatherFiveDay> response) {
                if(response.isSuccessful()){
                    view.onResponseFiveDayByName(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDay> call, Throwable t) {
                Log.i("error", t.getMessage());

            }
        });
    }


}
