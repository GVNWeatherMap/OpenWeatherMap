package minhnq.gvn.com.openweathermap.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.receiver.AlarmReceiver;
import minhnq.gvn.com.openweathermap.service.NotificationService;
import minhnq.gvn.com.openweathermap.utils.APIUtils;
import minhnq.gvn.com.openweathermap.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String NAME = "name";
    public static final String MAIN = "main";
    public static final String TEMP = "temp";
    private static final String TAG = "TAG";
    private Weathers weather = new Weathers();
    private WeatherFiveDay weatherFiveDay = new WeatherFiveDay();
    private TextView tvCityName, tvStatus, tvTemp;
    private RecyclerView rvFiveDay;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();
    private WeatherAdapter mAdapter;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();
        setAction();
        getWeatherFiveDay();
        getWeatherOneDay();
//        registerReceiver();
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallback();
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Common.current_location = locationResult.getLastLocation();

                Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation().getLatitude() + "/" + locationResult.getLastLocation().getLongitude());
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    private void initView() {
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
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
                    scheduleNotify(weather);
//                    startService(weather);
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
            @SuppressLint("WrongConstant")
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void scheduleNotify(Weathers weathers) {
        String name = weathers.name;
        String temp = String.valueOf(weathers.main.temp);
        String main = weathers.weather.get(0).main;

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(NAME, name);
        alarmIntent.putExtra(MAIN, main);
        alarmIntent.putExtra(TEMP, temp);
        alarmIntent.setAction("TEST");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.set(Calendar.HOUR_OF_DAY, 8);
        calendarAlarm.set(Calendar.MINUTE, 0);
        calendarAlarm.set(Calendar.SECOND, 0);
        calendarAlarm.set(Calendar.MILLISECOND, 0);
        long tringgerTime = calendarAlarm.getTimeInMillis();

        if (calendarAlarm.before(calendarNow)) {
            tringgerTime += 10000;
//            tringgerTime += 86400000L;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, tringgerTime, pendingIntent);

    }

}

