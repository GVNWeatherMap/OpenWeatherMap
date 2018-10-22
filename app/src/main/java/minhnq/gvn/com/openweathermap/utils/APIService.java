package minhnq.gvn.com.openweathermap.utils;

import minhnq.gvn.com.openweathermap.model.Weather;
import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("weather?q=Hanoi&units=metric&appid=56757f8c2e7f418d9cfb2dd63c961001")
    Call<Weathers> getWeatherDay();

    @GET("forecast?q=Hanoi&units=metric&cnt=5&appid=56757f8c2e7f418d9cfb2dd63c961001")
    Call<WeatherFiveDay> getWeatherFiveDay();

    @GET("weather")
    Call<Weathers> getWeatherByLatLng(@Query("lat") String lat,
                                      @Query("lon") String lng,
                                      @Query("appid") String appid,
                                      @Query("units") String unit);

    @GET("forecast")
    Call<WeatherFiveDay> getWeatherFiveDayByLatLng(@Query("lat") String lat,
                                             @Query("lon") String lng,
                                             @Query("cnt") int cnt,
                                             @Query("appid") String appid,
                                             @Query("units") String unit);
}
