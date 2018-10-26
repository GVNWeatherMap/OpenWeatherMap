package minhnq.gvn.com.openweathermap.model;

public class Location {

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
}
