package minhnq.gvn.com.openweathermap.utils;

import minhnq.gvn.com.openweathermap.model.WeatherFiveDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("weather?q=Hanoi&units=metric&appid=56757f8c2e7f418d9cfb2dd63c961001")
    Call<Weathers> getWeatherDay();

    @GET("forecast?q=Hanoi&units=metric&cnt=5&appid=56757f8c2e7f418d9cfb2dd63c961001")
    Call<WeatherFiveDay> getWeatherFiveDay();
}
