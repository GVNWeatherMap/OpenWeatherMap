package minhnq.gvn.com.openweathermap.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.List;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;
import minhnq.gvn.com.openweathermap.R;
import minhnq.gvn.com.openweathermap.adapter.WeatherAdapter;
import minhnq.gvn.com.openweathermap.broadcast.MyBroadcast;
import minhnq.gvn.com.openweathermap.constract.MainContract;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.WeatherOneDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import minhnq.gvn.com.openweathermap.presenter.MainPresenter;
import minhnq.gvn.com.openweathermap.service.UpdateService;
import minhnq.gvn.com.openweathermap.utils.Common;
import minhnq.gvn.com.openweathermap.utils.Constants;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TAG";
    private static final String WEATHER_SUNNY = "Sunny";
    private static final String WEATHER_RAIN = "Rain";
    private static final String WEATHER_CLEAR = "Clear";
    private TextView tvCityName, tvStatus, tvTemp;
    private ImageView imageView;
    private RecyclerView rvFiveDay;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WeatherAdapter mAdapter;
    private List<WeatherOneDay> listOneDay = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUpToolbar();
        setSwipeRefresh();
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filterF = new IntentFilter(Constants.ACTION_WEATHER_FIVEDAY);
        registerReceiver(broadcastWeatherFive, filterF);
        IntentFilter filterO = new IntentFilter(Constants.ACTION_WEATHER_ONEDAY);
        registerReceiver(broadcastWeatherOne, filterO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastWeatherFive);
        unregisterReceiver(broadcastWeatherOne);
    }

//    @Override
//    protected MainContract.IMainPresenter getPresenter() {
//        return new MainPresenter(this);
//    }

//    @Override
//    int getIdLayout() {
//        return R.layout.activity_main;
//    }

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
        imageView = findViewById(R.id.image_view);
        tvCityName = findViewById(R.id.txv_main_city_name);
        tvStatus = findViewById(R.id.txv_main_status);
        tvTemp = findViewById(R.id.txv_main_current_temp);
        rvFiveDay = findViewById(R.id.rv_main_weather_week);
        toolbar = findViewById(R.id.toolBar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
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
                Paper.init(MainActivity.this);
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                Paper.book().write(Constants.LOCATION_LAT, lat);
                Paper.book().write(Constants.LOCATION_LON, lon);
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                startService(intent);
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

    private MyBroadcast broadcastWeatherFive = new MyBroadcast() {
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

    private MyBroadcast broadcastWeatherOne = new MyBroadcast() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            String status = intent.getStringExtra(Constants.EXTRA_STT);

            switch (status) {
                case WEATHER_CLEAR:
                    imageView.setImageResource(R.drawable.bg_cloudy);
                    break;
                case WEATHER_SUNNY:
                    imageView.setImageResource(R.drawable.bg_sunny);
                    break;
                case WEATHER_RAIN:
                    imageView.setImageResource(R.drawable.bg_rainny);
                    break;
                default:
                    imageView.setImageResource(R.drawable.bg_cloudy);
                    break;
            }
            tvCityName.setText(intent.getStringExtra(Constants.EXTRA_CITY));
            tvStatus.setText(intent.getStringExtra(Constants.EXTRA_STATUS));
            tvTemp.setText(intent.getIntExtra(Constants.EXTRA_TEMP, 0) + "°C");
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_cloudy);
//        BitmapDrawable bg = new BitmapDrawable(getResizedBitmap(bm, 600));
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }
}

