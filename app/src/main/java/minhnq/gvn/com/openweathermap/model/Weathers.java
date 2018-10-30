package minhnq.gvn.com.openweathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Weathers implements Parcelable {

    @SerializedName("weather")
    @Expose
    public List<Weather> weather;

    @SerializedName("main")
    @Expose
    public Main main;

    @SerializedName("name")
    @Expose
    public String  name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.weather);
        dest.writeParcelable(this.main, flags);
        dest.writeString(this.name);
    }

    public Weathers() {
    }

    protected Weathers(Parcel in) {
        this.weather = new ArrayList<Weather>();
        in.readList(this.weather, Weather.class.getClassLoader());
        this.main = in.readParcelable(Main.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<Weathers> CREATOR = new Creator<Weathers>() {
        @Override
        public Weathers createFromParcel(Parcel source) {
            return new Weathers(source);
        }

        @Override
        public Weathers[] newArray(int size) {
            return new Weathers[size];
        }
    };

}
