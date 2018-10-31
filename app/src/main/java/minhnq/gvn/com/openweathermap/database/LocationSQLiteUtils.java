package minhnq.gvn.com.openweathermap.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import minhnq.gvn.com.openweathermap.model.Location;

public class LocationSQLiteUtils {

    private LocationOpenHelper dbHelper;
    private SQLiteDatabase db;



    public LocationSQLiteUtils createDatabase() throws SQLException {
        try {
            dbHelper.createDataBase();
        } catch (IOException ignored) {
        }
        return this;
    }

    public LocationSQLiteUtils open() throws SQLException {
        try {


            db = dbHelper.getReadableDatabase();
        } catch (SQLException ignored) {
        }
        return this;
    }



    //______________________________________________________________________________________________
    //__________________________________      QUERIES      ________________________________________
    //______________________________________________________________________________________________
    public List<Location> getCategories(int id) {
        String sql = "SELECT * FROM categories WHERE parent_id='" + id + "';";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            List<Location> categoriesName = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Location location  = new Location();
                    //ArrayList<Categories> categories = new ArrayList<>();
                    //categories.add(new Categories(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)));
                    location.locationid = cursor.getInt(0);
                    location.cityName = cursor.getString(1);
                    location.cityCode = cursor.getInt(2);
                    location.published = cursor.getInt(3);
                    location.ordering = cursor.getInt(4);
                } while (cursor.moveToNext());
            }
            return categoriesName;
        } catch (Exception ignored) {
        } finally {

        }
        return null;
    }



//    private SQLiteOpenHelper sqLiteOpenHelper;
//    private SQLiteDatabase sqLiteDatabase;
//    private String [] allColumn = {LocationOpenHelper.COLUMN_ID,LocationOpenHelper.COLUMN_CITYNAME,LocationOpenHelper.COLUMN_LONGTITUDE,
//            LocationOpenHelper.COLUMN_LONGTITUDE};
//    private Context context;
//    private static String DB_PATH = "";
//    public LocationSQLiteUtils(Context context){
//        this.context = context;
//        this.sqLiteOpenHelper = new LocationOpenHelper(context);
//    }
//
//    public void open(){
//        this.sqLiteDatabase =sqLiteOpenHelper.getWritableDatabase();
//    }
//
//
//
//    public Location cursorModel(Cursor cursor){
//        Location location = new Location();
//        location.locationid = cursor.getInt(0);
//        location.cityName  = cursor.getString(1);
//        location.cityCode = cursor.getInt(2);
//        location.published = cursor.getInt(3);
//        location.ordering = cursor.getInt(4);
//        return location;
//    }
//
//    public Location getLocation(int id){
//
//        Cursor cursor = sqLiteDatabase.query(LocationOpenHelper.TABLE_NAME, allColumn, LocationOpenHelper.COLUMN_ID+ " =?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//
//        if(cursor!=null){
//            cursor.moveToFirst();
//        }
//
//        Location location = cursorModel(cursor);
//        return location;
//    }
//
//    public List<Location> getAllLocation(){
//        List<Location> locationList = new ArrayList<>();
//        String query = "SELECT * FROM " + LocationOpenHelper.TABLE_NAME;
//        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
//        Log.i("cursor", String.valueOf(cursor.getCount()));
//        cursor.moveToFirst();
//        if(cursor.moveToFirst()){
//            while (!cursor.isAfterLast()){
//                Location location = cursorModel(cursor);
//                locationList.add(location);
//                cursor.moveToNext();
//            }
//        }
//        return locationList;
//    }
//
////    public void addLocation(Location location) {
////        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
////
////        ContentValues values = new ContentValues();
////        values.put(LocationOpenHelper.COLUMN_CITYNAME, location.cityName);
////        values.put(LocationOpenHelper.COLUMN_LONGTITUDE, location.longtitude);
////        values.put(LocationOpenHelper.COLUMN_LATTITUDE, location.lattitude);
////        // Trèn một dòng dữ liệu vào bảng.
////        db.insert(LocationOpenHelper.TABLE_NAME, null, values);
////        // Đóng kết nối database.
////
////    }
//
//    //if database is empty
//    public void dumpData(){
//        int count = this.getNotesCount();
//        if(count ==0){
//
//        }
//
//
//    }
//    public int getNotesCount() {
//        String countQuery = "SELECT  * FROM " + LocationOpenHelper.TABLE_NAME;
//        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        int count = cursor.getCount();
//
//        // return count
//        return count;
//    }
//
//    void createDataBase() throws IOException {
//        if(!checkDataBase()) {
//            sqLiteOpenHelper.getReadableDatabase();
//            copyDataBase();
//            this.close();
//        }
//    }
//
//    private boolean checkDataBase() {
//        File DbFile = new File(DB_PATH + LocationOpenHelper.DATABASE_NAME);
//        return DbFile.exists();
//    }
//    private void copyDataBase() throws IOException {
//        InputStream mInput =  context.getAssets().open(LocationOpenHelper.DATABASE_NAME);
//        String outfileName = DB_PATH;
//        OutputStream mOutput = new FileOutputStream(outfileName);
//        byte[] buffer = new byte[1024];
//        int mLength;
//        while ((mLength = mInput.read(buffer))>0) {
//            mOutput.write(buffer, 0, mLength);
//        }
//        mOutput.flush();
//        mInput.close();
//        mOutput.close();
//    }

}
