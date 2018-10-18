package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class WeatherOneDay {

//    @SerializedName("temp")
//    @Expose
//    public Temp temp;

    @SerializedName("weather")
    @Expose
    public List<Weather> weather;

    @SerializedName("main")
    @Expose
    public Main main;

    @SerializedName("dt_txt")
    @Expose
    public String dt_txt;

}
