package minhnq.gvn.com.openweathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherOneDay implements Parcelable {

    @SerializedName("weather")
    @Expose
    public List<Weather> weather;

    @SerializedName("dt")
    public long dt;

    @SerializedName("main")
    @Expose
    public Main main;

    @SerializedName("dt_txt")
    @Expose
    public String dt_txt;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.weather);
        dest.writeLong(this.dt);
        dest.writeParcelable(this.main, flags);
        dest.writeString(this.dt_txt);
    }

    public WeatherOneDay() {
    }

    protected WeatherOneDay(Parcel in) {
        this.weather = in.createTypedArrayList(Weather.CREATOR);
        this.dt = in.readLong();
        this.main = in.readParcelable(Main.class.getClassLoader());
        this.dt_txt = in.readString();
    }

    public static final Creator<WeatherOneDay> CREATOR = new Creator<WeatherOneDay>() {
        @Override
        public WeatherOneDay createFromParcel(Parcel source) {
            return new WeatherOneDay(source);
        }

        @Override
        public WeatherOneDay[] newArray(int size) {
            return new WeatherOneDay[size];
        }
    };
}
