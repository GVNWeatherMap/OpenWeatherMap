package minhnq.gvn.com.openweathermap.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.SearchLocationAdapter;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.broadcast.MyBroadcast;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.service.NotificationService;
import minhnq.gvn.com.openweathermap.service.UpdateByNameService;
import minhnq.gvn.com.openweathermap.service.UpdateService;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TAG";
    private static final String WEATHER_RAINNY = "Rain";
    private static final String WEATHER_SUNNY = "Sunny";
    private static final String WEATHER_CLEAR = "Clear";
    private static final String WEATHER_MIST = "Mist";
    private TextView tvCityName, tvStatus, tvTemp;
    private ImageView imageView;
    private AppCompatAutoCompleteTextView mAutoCompleteTextView;
    private RecyclerView rvFiveDay;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WeatherAdapter mAdapter;
    private SearchLocationAdapter searchLocationAdapter;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();
    private List<Location> listLocation = new ArrayList<>();
    private String cityNameAccented;
    private LinearLayout mLinearLayout;
    private static double lastLat;
    private static double lastLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUpToolbar();
        setSwipeRefresh();
        requestPermission();

    }

    private void setSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register five day
        registerBroadcast(broadcastWeatherFive, Constants.ACTION_WEATHER_FIVEDAY);
        //register one day
        registerBroadcast(broadcastWeatherOne, Constants.ACTION_WEATHER_ONEDAY);
        // register search
        registerBroadcast(searchLocationReceiver, Constants.ACTION_WEATHER_SEARCH);
        //register one day by name
        registerBroadcast(broadcastWeatherOneByName, Constants.ACTION_WEATHER_ONEDAY_BY_NAME);
        //register five day by name
        registerBroadcast(broadcastWeatherFiveByDay, Constants.ACTION_WEATHER_FIVEDAY_BY_NAME);
        //register error
        registerBroadcast(broadcastError, Constants.ACTION_WEATHER_ERROR);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastWeatherFive);
        unregisterReceiver(broadcastWeatherOne);
        unregisterReceiver(searchLocationReceiver);
        unregisterReceiver(broadcastWeatherOneByName);
        unregisterReceiver(broadcastWeatherFiveByDay);
        unregisterReceiver(broadcastError);
        Intent intent = new Intent(MainActivity.this, UpdateByNameService.class);
        stopService(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        requestPermission();
    }

    private void initView() {
        imageView = findViewById(R.id.image_view);
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);
        toolbar = findViewById(R.id.toolBar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationView = findViewById(R.id.nav_view);
        mLinearLayout = findViewById(R.id.ln_main_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        mAutoCompleteTextView = findViewById(R.id.act_autoTextView);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                menuItem.setChecked(true);
////                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void registerBroadcast(BroadcastReceiver broadcastReceiver, String action) {
        IntentFilter intentFilter = new IntentFilter(action);
        registerReceiver(broadcastReceiver, intentFilter);
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

                            if (ActivityCompat.checkSelfPermission(
                                    MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(
                                            MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Log.d(TAG, "onPermissionRationaleShouldBeShown: ");
                    }
                }).check();
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Paper.init(MainActivity.this);
                lastLat = locationResult.getLastLocation().getLatitude();
                lastLon = locationResult.getLastLocation().getLongitude();
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                Bundle bundle = new Bundle();
                bundle.putDouble(Constants.EXTRA_LAT, lastLat);
                bundle.putDouble(Constants.EXTRA_LONG, lastLon);
                intent.putExtra(Constants.EXTRA_BUNDLE_LOCATION, bundle);
                startService(intent);
                scheduleNotify();
//                getIntentService(lat, lon);
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

    private void scheduleNotify() {
        Intent notiIntent = new Intent(this, NotificationService.class);
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.EXTRA_LAT, lastLat);
        bundle.putDouble(Constants.EXTRA_LONG, lastLon);
        notiIntent.putExtra(Constants.EXTRA_BUNDLE_LOCATION, bundle);
        Calendar now = Calendar.getInstance();
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.set(Calendar.HOUR_OF_DAY, 8);
        calendarAlarm.set(Calendar.MINUTE, 0);
        calendarAlarm.set(Calendar.SECOND, 0);
        if (now.after(calendarAlarm)) {
            Log.d("Hey", "Added a day");
            calendarAlarm.add(Calendar.DATE, 1);

        }
        calendarAlarm.add(Calendar.SECOND, 1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendarAlarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private BroadcastReceiver broadcastWeatherFive = new MyBroadcast() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            mAdapter = new WeatherAdapter(MainActivity.this);
            Bundle bundle = intent.getExtras();
            WeatherFiveDay fiveDay = bundle.getParcelable(Constants.EXTRA_WEATHER_FIVEDAY);
            listOneDay = fiveDay.list;
            mAdapter.setDatas(listOneDay);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            rvFiveDay.setLayoutManager(layoutManager);
            rvFiveDay.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private BroadcastReceiver broadcastWeatherOne = new MyBroadcast() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Bundle bundle = intent.getExtras();
            Weathers weathers = bundle.getParcelable(Constants.EXTRA_WEATHER_ONEDAY);
            Paper.init(MainActivity.this);
            Paper.book().write(Constants.EXTRA_TEMP, weathers.main.temp);
            Paper.book().write(Constants.EXTRA_CITY_NAME, weathers.name);
            String status = weathers.weather.get(0).main;
            switch (status) {
                case WEATHER_CLEAR:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_cloudy);
                    break;
                case WEATHER_SUNNY:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_sunny);
                    break;
                case WEATHER_RAINNY:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_rainny);
                    break;
                case WEATHER_MIST:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_mist);
                    break;
                default:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_rainny);
                    break;
            }
            tvCityName.setText(weathers.name);
            tvStatus.setText(status);
            int temp = (int) (weathers.main.temp);
            tvTemp.setText(String.valueOf(temp) + "°C");
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private BroadcastReceiver searchLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(UpdateService.EXTRA_BUNDLE_SEARCH);
            listLocation = bundle.getParcelableArrayList(UpdateService.EXTRA_LIST_SEARCH);
            searchLocationAdapter = new SearchLocationAdapter(MainActivity.this, R.layout.row_item_navigation_all_location,
                    listLocation);
            mAutoCompleteTextView.setThreshold(1);
            mAutoCompleteTextView.setAdapter(searchLocationAdapter);
            mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Location location = (Location) adapterView.getItemAtPosition(i);
                    cityNameAccented = location.cityName;
                    Intent intent = new Intent(MainActivity.this, UpdateByNameService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.EXTRA_CITY_NAME, Common.removeAccent(location.cityName));
//                    bundle.putString(Constants.EXTRA_CITY_NAME_ACCENTED,location.cityName);
                    intent.putExtra(Constants.EXTRA_BUNDLE_CITY_NAME, bundle);
                    Paper.init(MainActivity.this);
                    Paper.book().write(Constants.EXTRA_CITY_NAME_ACCENTED, location.cityName);
                    startService(intent);
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    mLinearLayout.requestFocus();
                }
            });
        }
    };

    private BroadcastReceiver broadcastWeatherOneByName = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Weathers weathers = bundle.getParcelable(Constants.EXTRA_WEATHER_ONEDAY);
            String status = weathers.weather.get(0).main;
            switch (status) {
                case WEATHER_CLEAR:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_cloudy);
                    break;
                case WEATHER_SUNNY:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_sunny);
                    break;
                case WEATHER_RAINNY:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_rainny);
                    break;
                case WEATHER_MIST:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_mist);
                    break;
                default:
                    mLinearLayout.setBackgroundResource(R.drawable.bg_rainny);
                    break;
            }
            tvCityName.setText(cityNameAccented);
            tvStatus.setText(status);
            int temp = (int) (weathers.main.temp);
            tvTemp.setText(String.valueOf(temp) + "°C");
        }
    };

    private BroadcastReceiver broadcastWeatherFiveByDay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter = new WeatherAdapter(MainActivity.this);
            Bundle bundle = intent.getExtras();
            WeatherFiveDay fiveDay = bundle.getParcelable(Constants.EXTRA_WEATHER_FIVEDAY);
            listOneDay = fiveDay.list;
            mAdapter.setDatas(listOneDay);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            rvFiveDay.setLayoutManager(layoutManager);
            rvFiveDay.setAdapter(mAdapter);
        }
    };

    private BroadcastReceiver broadcastError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString(Constants.EXTRA_WEATHER_ERROR);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}