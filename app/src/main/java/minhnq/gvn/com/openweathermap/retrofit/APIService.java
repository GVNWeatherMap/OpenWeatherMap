package minhnq.gvn.com.openweathermap.retrofit;

import minhnq.gvn.com.openweathermap.model.WeatherWeekDay;
import minhnq.gvn.com.openweathermap.model.Weathers;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("weather?q=Hanoi&units=metric&appid=56757f8c2e7f418d9cfb2dd63c961001")
    Call<Weathers> getWeatherDay();

    @GET("forecast/daily?id=524901&appid=b1b15e88fa797225412429c1c50c122a1")
    Call<WeatherWeekDay> getWeather7Day();

}
