package minhnq.gvn.com.openweathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    public int locationid;
    public String cityName;
    public int cityCode;
    public int published;
    public int ordering;

    public Location(int locationid, String cityName, int cityCode, int published, int ordering) {
        this.locationid = locationid;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.published = published;
        this.ordering = ordering;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.locationid);
        dest.writeString(this.cityName);
        dest.writeInt(this.cityCode);
        dest.writeInt(this.published);
        dest.writeInt(this.ordering);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.locationid = in.readInt();
        this.cityName = in.readString();
        this.cityCode = in.readInt();
        this.published = in.readInt();
        this.ordering = in.readInt();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
