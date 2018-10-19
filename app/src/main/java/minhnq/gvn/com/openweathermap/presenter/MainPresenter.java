package minhnq.gvn.com.openweathermap.presenter;


import minhnq.gvn.com.openweathermap.constract.MainContract;
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
        APIUtils.getAPIService().getWeatherByLatLng(lat,lng,appid,unit).enqueue(new Callback<Weathers>() {
            @Override
            public void onResponse(Call<Weathers> call, Response<Weathers> response) {
                view.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Weathers> call, Throwable t) {

            }
        });
    }
}
