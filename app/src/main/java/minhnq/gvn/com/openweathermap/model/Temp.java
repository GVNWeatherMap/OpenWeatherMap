package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temp {

    @SerializedName("day")
    @Expose
    public double day;

    @SerializedName("min")
    @Expose
    public double min;

    @SerializedName("max")
    @Expose
    public double max;

    @SerializedName("night")
    @Expose
    public double night;
}
