package minhnq.gvn.com.openweathermap.utils;

import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("weather")
    Call<Weathers> getWeatherByName(@Query(value = "q", encoded = true) String city,
                                    @Query("appid") String appid,
                                    @Query("units") String unit);
//
    @GET("forecast")
    Call<WeatherFiveDay> getWeatherFiveDayByName(@Query(value = "q", encoded = true) String city,
                                                 @Query("cnt") int cnt,
                                           @Query("appid") String appid,
                                           @Query("units") String unit);

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
