package minhnq.gvn.com.openweathermap.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    public int locationid;
    public String cityName;
    public double longtitude;
    public double lattitude;

    public Location(String name, double longtitude, double lattitude) {
        this.cityName = name;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public Location() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.locationid);
        dest.writeString(this.cityName);
        dest.writeDouble(this.longtitude);
        dest.writeDouble(this.lattitude);
    }

    protected Location(Parcel in) {
        this.locationid = in.readInt();
        this.cityName = in.readString();
        this.longtitude = in.readDouble();
        this.lattitude = in.readDouble();
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
