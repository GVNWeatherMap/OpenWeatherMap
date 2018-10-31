package minhnq.gvn.com.openweathermap.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import minhnq.gvn.com.openweathermap.model.Location;

public class LocationOpenHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "Location1_db";
    private SQLiteDatabase db;
    private Context context;
    private String DB_PATH;

    public LocationOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + "minhnq.gvn.com.openweathermap/" + "databases/";
    }

    public LocationOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {

        String path = DB_PATH + DB_NAME;
        File dbFile = new File(path);
        Log.i("database", dbFile.getPath());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public ArrayList<Location> getDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Location> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT * FROM 'provinces' ", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Location location = new Location(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4));
                list.add(location);
            }
            cursor.close();
            db.close();
        }
        return list;
    }



}
