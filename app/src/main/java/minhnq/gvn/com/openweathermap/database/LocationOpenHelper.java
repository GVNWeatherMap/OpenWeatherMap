package minhnq.gvn.com.openweathermap.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class LocationOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = "SQLite";
    public static final String DATABASE_NAME = "Location_db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "location";
    public static final String COLUMN_ID = "location_id";
    public static final String COLUMN_CITYNAME = "cityname";
    public static final String COLUMN_LONGTITUDE = "longtitude";
    public static final String COLUMN_LATTITUDE = "lattitude";


    public LocationOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CITYNAME + " TEXT,"
                + COLUMN_LONGTITUDE + " INTEGER,"
                + COLUMN_LATTITUDE + " INTEGER" + ")";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Và tạo lại.
        onCreate(sqLiteDatabase);
    }
}
