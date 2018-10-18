package minhnq.gvn.com.openweathermap.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.utils.APIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private Weathers weather = new Weathers();
    private WeatherFiveDay weatherFiveDay = new WeatherFiveDay();
    private TextView tvCityName, tvStatus, tvTemp;
    private RecyclerView rvFiveDay;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();
    private WeatherAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getWeatherFiveDay();
        getWeatherOneDay();


    }

    private void initView() {
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);

    }

    private void getWeatherOneDay() {
        APIUtils.getAPIService().getWeatherDay().enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                if (response.isSuccessful()) {
                    weather = response.body();
                    tvCityName.setText(weather.name);
                    tvStatus.setText(weather.weather.get(0).main);
                    tvTemp.setText(String.valueOf(weather.main.temp) + "°C");
                }
            }

            @Override
            public void onFailure(Call<Weathers> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }

    private void getWeatherFiveDay() {
        APIUtils.getAPIService().getWeatherFiveDay().enqueue(new Callback<WeatherFiveDay>() {
            @Override
            public void onResponse(Call<WeatherFiveDay> call, Response<WeatherFiveDay> response) {
                if (response.isSuccessful()) {
                    weatherFiveDay = response.body();
                    listOneDay = weatherFiveDay.list;

                    mAdapter = new WeatherAdapter(MainActivity.this);
                    mAdapter.setDatas(listOneDay);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvFiveDay.setLayoutManager(layoutManager);
                    rvFiveDay.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDay> call, Throwable t) {
                Log.i("onFailure", t.getMessage());
            }
        });
    }

}

