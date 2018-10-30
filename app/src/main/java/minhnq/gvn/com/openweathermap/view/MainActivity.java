package minhnq.gvn.com.openweathermap.view;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.LocationApdater;
import minhnq.gvn.com.openweathermap.adapter.SearchLocationAdapter;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.receiver.AlarmReceiver;
import minhnq.gvn.com.openweathermap.service.UpdateService;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.widget.WeatherWidget;


public class MainActivity extends BaseActivity<MainContract.IMainPresenter> implements
        SwipeRefreshLayout.OnRefreshListener, MainContract.IMainView, LocationApdater.ILocationListener {
    private static final String TAG = "TAG";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_MAIN = "main";
    public static final String EXTRA_TEMP = "temp";
    public static final String EXTRA_WEATHER_ONE_DAY = "oneDay";
    public static final String EXTRA_WEATHER_FIVE_DAY = "fiveDay";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGTITUDE = "longtitude";
    public static final String EXTRA_BUNDLE_LOCATION = "location";

    private static int COUNT_DAY = 5;
    private TextView tvCityName, tvStatus, tvTemp;
    private AppCompatAutoCompleteTextView mAutoCompleteTextView;
    private RecyclerView rvFiveDay, rvAllLocation;
    private EditText edtSearch;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WeatherAdapter mAdapter;
    private SearchLocationAdapter searchLocationAdapter;
    private LocationApdater mLocationApdater;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();
    private List<Location> listLocation = new ArrayList<>();
    private List<Location> listAutoText = new ArrayList<>();
    List<Location> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startService(new Intent(this,UpdateService.class));
        requestPermission();
        initView();
        setUpToolbar();
        setAction();
        presenter.getAllLocation(this);
//        mLocationApdater.initILocationListener(this);
//        startService(new Intent(this,UpdateService.class));

        IntentFilter intentFilterOneDay = new IntentFilter();
        intentFilterOneDay.addAction(UpdateService.ACTION_ONE_DAY);

        registerReceiver(oneDayReceiver, intentFilterOneDay);
        IntentFilter intentFilterFiveDay = new IntentFilter(UpdateService.ACTION_FIVE_DAY);
        registerReceiver(fiveDayReceiver, intentFilterFiveDay);
        IntentFilter intentListLocation = new IntentFilter(UpdateService.ACITON_SEARCH);
        registerReceiver(searchLocationReceiver,intentListLocation);

    }

    @Override
    public void onResponse(Weathers weather) {
//        int temp = (int) weather.main.temp;
////        tvCityName.setText(weather.name);
////        tvStatus.setText(weather.weather.get(0).main);
////        tvTemp.setText(temp + "°C");
////        swipeRefreshLayout.setRefreshing(false);
////
//        Paper.init(this);
//        Paper.book().write("city", weather.name);
//        Paper.book().write("temp", temp);
////        //get data for notification
//        scheduleNotify(weather);
    }

    @Override
    public void onResponeFiveDay(WeatherFiveDay weatherFiveDay) {
//        mAdapter = new WeatherAdapter(MainActivity.this);
//        listOneDay = weatherFiveDay.list;
//        mAdapter.setDatas(listOneDay);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        rvFiveDay.setLayoutManager(layoutManager);
//        rvFiveDay.setAdapter(mAdapter);
//        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResponeAllLocation(List<Location> list) {
//        listLocation = list;
//        Log.i(TAG, String.valueOf(list.size()));
//        mLocationApdater = new LocationApdater(this);
//        mLocationApdater.setDatas(listLocation);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        rvAllLocation.setLayoutManager(layoutManager);
//        rvAllLocation.setAdapter(mLocationApdater);
    }

    @Override
    protected MainContract.IMainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    int getIdLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
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
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        edtSearch = findViewById(R.id.edt_main_search_location);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);
        rvAllLocation = findViewById(R.id.rv_navigation_all_location);
        toolbar = findViewById(R.id.toolBar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        mAutoCompleteTextView = findViewById(R.id.act_autoTextView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchList.clear();
                String strSearch = charSequence.toString();
                strSearch = strSearch.replace(".", "");
                strSearch = strSearch.replace(" ", "");
                for (Location l : listLocation) {
                    if (l.cityName.toLowerCase().contains(strSearch.toLowerCase())) {
                        searchList.add(l);
                    }
                }
                mLocationApdater.setDatas(searchList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                rvAllLocation.setLayoutManager(layoutManager);
                rvAllLocation.setAdapter(mLocationApdater);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Weather");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                getIntentService(lat, lon);
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

    private void getWeatherOneDay(double lat, double lon) {
        presenter.getWeatherNow(String.valueOf(lat),
                String.valueOf(lon),
                Common.APP_ID, "metric");
    }

    private void getWeatherFiveDay(double lat, double lon) {

        presenter.getWeatherFiveDay(String.valueOf(lat),
                String.valueOf(lon),
                COUNT_DAY,
                Common.APP_ID, "metric");
    }

    private void scheduleNotify(Weathers weathers) {
        String name = weathers.name;
        String temp = String.valueOf(weathers.main.temp);
        String main = weathers.weather.get(0).main;
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(EXTRA_NAME, name);
        alarmIntent.putExtra(EXTRA_MAIN, main);
        alarmIntent.putExtra(EXTRA_TEMP, temp);
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
            tringgerTime += 86400000L;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, tringgerTime, pendingIntent);
    }

    private void getIntentService(double lat, double lon) {
        Intent intentService = new Intent(getApplicationContext(), UpdateService.class);
        Bundle bundleService = new Bundle();
        bundleService.putDouble(EXTRA_LATITUDE, lat);
        bundleService.putDouble(EXTRA_LONGTITUDE, lon);
        intentService.putExtra(EXTRA_BUNDLE_LOCATION, bundleService);
        startService(intentService);
    }


    private void setAction() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(oneDayReceiver);
        unregisterReceiver(fiveDayReceiver);
        unregisterReceiver(searchLocationReceiver);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_HOME){
            Toast.makeText(this, "Home button", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClickLocation(Location location) {

        getIntentService(location.lattitude, location.longtitude);

        drawerLayout.closeDrawers();
    }

    private BroadcastReceiver oneDayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(UpdateService.EXTRA_BUNDLE_ONE_DAY);
            Weathers weather = bundle.getParcelable(EXTRA_WEATHER_ONE_DAY);
            int temp = (int) weather.main.temp;
            tvCityName.setText(weather.name);
            tvStatus.setText(weather.weather.get(0).main);
            tvTemp.setText(temp + "°C");
            swipeRefreshLayout.setRefreshing(false);
            Paper.init(MainActivity.this);
            Paper.book().write("city", weather.name);
            Paper.book().write("temp", temp);
//            get data for notification
            updateWidget();
            scheduleNotify(weather);

        }
    };

    private BroadcastReceiver fiveDayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(UpdateService.EXTRA_BUNDLE_FIVE_DAY);
            WeatherFiveDay weatherFiveDay = bundle.getParcelable(EXTRA_WEATHER_FIVE_DAY);
            mAdapter = new WeatherAdapter(MainActivity.this);
            listOneDay = weatherFiveDay.list;
            mAdapter.setDatas(listOneDay);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            rvFiveDay.setLayoutManager(layoutManager);
            rvFiveDay.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);

        }
    };

    private BroadcastReceiver searchLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(UpdateService.EXTRA_BUNDLE_SEARCH);
            listLocation = bundle.getParcelableArrayList(UpdateService.EXTRA_LIST_SEARCH);
            mLocationApdater = new LocationApdater(MainActivity.this);
            mLocationApdater.setDatas(listLocation);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            rvAllLocation.setLayoutManager(layoutManager);
            rvAllLocation.setAdapter(mLocationApdater);

            searchLocationAdapter = new SearchLocationAdapter(MainActivity.this,R.layout.row_item_navigation_all_location,
                    listLocation);
            mAutoCompleteTextView.setThreshold(1);
            mAutoCompleteTextView.setAdapter(searchLocationAdapter);
            mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Location location = (Location) adapterView.getItemAtPosition(i);
                    getIntentService(location.lattitude,location.longtitude);
                }
            });
        }
    };
    private void updateWidget(){
        Intent intent1 = new Intent(this, WeatherWidget.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WeatherWidget.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent1);
    }


}