package minhnq.gvn.com.openweathermap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import minhnq.gvn.com.openweathermap.model.Location;

public class LocationSQLiteUtils {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String [] allColumn = {LocationOpenHelper.COLUMN_ID,LocationOpenHelper.COLUMN_CITYNAME,LocationOpenHelper.COLUMN_LONGTITUDE,
            LocationOpenHelper.COLUMN_LONGTITUDE};
    private Context context;

    public LocationSQLiteUtils(Context context){
        this.context = context;
        this.sqLiteOpenHelper = new LocationOpenHelper(context);
    }

    public void open(){
        this.sqLiteDatabase =sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteOpenHelper.close();
    }

    public Location cursorModel(Cursor cursor){
        Location location = new Location();
        location.locationid = cursor.getInt(0);
        location.cityName  = cursor.getString(1);
        location.longtitude = cursor.getInt(2);
        location.lattitude = cursor.getInt(3);
        return location;
    }

    public Location getLocation(int id){

        Cursor cursor = sqLiteDatabase.query(LocationOpenHelper.TABLE_NAME, allColumn, LocationOpenHelper.COLUMN_ID+ " =?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }

        Location location = cursorModel(cursor);
        return location;
    }

    public List<Location> getAllLocation(){
        List<Location> locationList = new ArrayList<>();
        String query = "SELECT * FROM " + LocationOpenHelper.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        Log.i("cursor", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Location location = cursorModel(cursor);
                locationList.add(location);
                cursor.moveToNext();
            }
        }
        return locationList;
    }

    public void addLocation(Location location) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationOpenHelper.COLUMN_CITYNAME, location.cityName);
        values.put(LocationOpenHelper.COLUMN_LONGTITUDE, location.longtitude);
        values.put(LocationOpenHelper.COLUMN_LATTITUDE, location.lattitude);
        // Trèn một dòng dữ liệu vào bảng.
        db.insert(LocationOpenHelper.TABLE_NAME, null, values);
        // Đóng kết nối database.

    }

    //if database is empty
    public void dumpData(){
        int count = this.getNotesCount();
        if(count ==0){
            Location location1 = new Location("Ha Noi",105.825179,21.028053);
            Location location2 = new Location("Ho Chi Minh",106.660172,10.762622);
            Location location3 = new Location("Da Nang",108.206230,16.047079);
            Location location4 = new Location("New York",-73.935242,40.730610);
            addLocation(location1);
            addLocation(location2);
            addLocation(location3);
            addLocation(location4);
        }


    }
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + LocationOpenHelper.TABLE_NAME;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        // return count
        return count;
    }


}
