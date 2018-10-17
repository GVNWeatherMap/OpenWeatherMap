package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weathers {

    @SerializedName("weather")
    @Expose
    public Weather weather;

    @SerializedName("main")
    @Expose
    public Main main;

    @SerializedName("name")
    @Expose
    public String  name;

}
