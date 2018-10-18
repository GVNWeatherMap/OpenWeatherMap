package minhnq.gvn.com.openweathermap.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.Utils.ConvertTemp;
import minhnq.gvn.com.openweathermap.model.WeatherWeekDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.retrofit.APIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Weathers mWeathers = new Weathers();
    private WeatherWeekDay mWeather7day = new WeatherWeekDay();
    private TextView tvCityName, tvStatus, tvTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        getWeahterWeekDay();
        getWeatherOneDay();

    }

    void getWeatherOneDay(){
        APIUtils.getAPIService().getWeatherDay().enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                if(response.isSuccessful()){
                    mWeathers = response.body();
                    tvCityName.setText(mWeathers.name);
                    tvStatus.setText(mWeathers.weather.get(0).main);
                    tvTemp.setText(String.valueOf(mWeathers.main.temp));

                    Log.i("name",mWeathers.name);
                    Log.i("weather", String.valueOf(mWeathers.weather.size()));
                }
            }

            @Override
            public void onFailure(Call<Weathers> call, Throwable t) {
                Log.i("errorOne", t.getMessage());
            }
        });
    }

    void getWeahterWeekDay(){

        APIUtils.getAPIService().getWeather7Day().enqueue(new Callback<WeatherWeekDay>() {
            @Override
            public void onResponse(Call<WeatherWeekDay> call, Response<WeatherWeekDay> response) {
                if(response.isSuccessful()){
                    mWeather7day = response.body();
                    Log.i("name",mWeather7day.list.get(0).weather.get(0).main);
                }
            }

            @Override
            public void onFailure(Call<WeatherWeekDay> call, Throwable t) {
                Log.i("errorWeek", t.getMessage());

            }
        });
    }

}

