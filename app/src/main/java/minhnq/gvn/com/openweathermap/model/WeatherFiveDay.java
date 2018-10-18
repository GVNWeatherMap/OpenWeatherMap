package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherFiveDay {

    @SerializedName("list")
    @Expose
    public List<WeatherOneDay> list;
}
