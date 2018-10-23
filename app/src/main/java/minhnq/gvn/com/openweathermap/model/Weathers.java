package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weathers  {

    @SerializedName("weather")
    @Expose
    public List<Weather> weather;

    @SerializedName("main")
    @Expose
    public Main main;

    @SerializedName("name")
    @Expose
    public String  name;

}
