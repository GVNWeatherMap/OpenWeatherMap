package minhnq.gvn.com.openweathermap.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.model.Main;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.utils.APIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TAG";
    private Weathers weather = new Weathers();
    private WeatherFiveDay weatherFiveDay = new WeatherFiveDay();
    private TextView tvCityName, tvStatus, tvTemp;
    private RecyclerView rvFiveDay;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();
    private WeatherAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setAction();
        getWeatherFiveDay();
        getWeatherOneDay();


    }

    private void initView() {
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

    }

    public void sendNotification() {
                NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_notify)
                        .setContentTitle("Weather Day")
                        .setContentText("Test");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

    private void setAction() {
        swipeRefreshLayout.setOnRefreshListener(this);
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

                    Intent notifyIntent = new Intent(MainActivity.this, MainActivity.class);
// Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            MainActivity.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder.setSmallIcon(R.drawable.icon_notify)
                            .setContentTitle("Weather Now")
                            .setContentText(weather.name + ": " + weather.weather.get(0).main + " - " + String.valueOf(weather.main.temp) + "°C");
                    mBuilder.setContentIntent(notifyPendingIntent);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
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
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDay> call, Throwable t) {
                Log.i("onFailure", t.getMessage());

            }
        });
    }

    @Override
    public void onRefresh() {
        getWeatherOneDay();
        getWeatherFiveDay();
//        sendNotification();

    }
}

