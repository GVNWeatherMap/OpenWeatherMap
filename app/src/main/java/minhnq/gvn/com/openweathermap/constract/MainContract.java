package minhnq.gvn.com.openweathermap.constract;

import android.content.Context;

import java.util.List;

import minhnq.gvn.com.openweathermap.constract.base.BaseContract;
import minhnq.gvn.com.openweathermap.constract.base.IPresenterCallBack;
import minhnq.gvn.com.openweathermap.constract.base.IViewCallBack;
import minhnq.gvn.com.openweathermap.model.Location;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;

public interface MainContract extends BaseContract {

    interface IMainView extends IViewCallBack {
        void onResponse(Weathers weather);

        void onResponseFiveDay(WeatherFiveDay weatherFiveDay);

        void onGetAllLocation(List<Location> locationList);

        void onResponceNowByName(Weathers weathers);

        void onResponseFiveDayByName(WeatherFiveDay weatherFiveDay);
        void onResponseError(String error);
    }

    interface IMainPresenter extends IPresenterCallBack {
        void getWeatherNow(String lat,
                           String lng,
                           String appid,
                           String unit);

        void getWeatherFiveDay(String lat,
                               String lng,
                               int cnt,
                               String appid,
                               String unit);

        void getAllLocation(Context context);

        void getWeatherNowByName(String city,
                                 String appid,
                                 String unit);

        void getWeatherFiveDayByName(String city,
                                     int cnt,
                                     String appid,
                                     String unit);
    }
}
