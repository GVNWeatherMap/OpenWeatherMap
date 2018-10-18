package minhnq.gvn.com.openweathermap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    @Expose
    public int temp;

    @SerializedName("temp_min")
    @Expose
    public double temp_min;

    @SerializedName("temp_max")
    @Expose
    public double temp_max;



}
