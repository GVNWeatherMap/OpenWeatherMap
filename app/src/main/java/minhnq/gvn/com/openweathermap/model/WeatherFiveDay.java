package minhnq.gvn.com.openweathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherFiveDay implements Parcelable {

    @SerializedName("list")
    @Expose
    public List<WeatherOneDay> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
    }

    public WeatherFiveDay() {
    }

    protected WeatherFiveDay(Parcel in) {
        this.list = new ArrayList<>();
        in.readList(this.list, WeatherOneDay.class.getClassLoader());
    }

    public static final Creator<WeatherFiveDay> CREATOR = new Creator<WeatherFiveDay>() {
        @Override
        public WeatherFiveDay createFromParcel(Parcel source) {
            return new WeatherFiveDay(source);
        }

        @Override
        public WeatherFiveDay[] newArray(int size) {
            return new WeatherFiveDay[size];
        }
    };
}
