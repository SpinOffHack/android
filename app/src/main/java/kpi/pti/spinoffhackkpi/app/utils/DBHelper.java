package kpi.pti.spinoffhackkpi.app.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locations";
    private static final String TABLE_LOCATIONS = "locations";

    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN = "login";
    //private static final String KEY_PASSWORD = "password";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_TIME = "Timestamp";

    //private static final String[] COLUMNS = {KEY_ID,KEY_LOGIN,KEY_PASSWORD,KEY_X,KEY_Y,KEY_TIME};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE locations ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT, "+
                "x REAL, y REAL,Timestamp TEXT)";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOCATIONS);
        this.onCreate(db);
    }

    public void addLocate(Locality locality,User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, user.getLogin());
        values.put(KEY_X,locality.getLatitude());
        values.put(KEY_Y,locality.getLongitude());
        values.put(KEY_TIME, String.valueOf(locality.getTimestamp()));
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    public ArrayList<Locality> getLocationList(User user){
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + "WHERE login = '"+user.getLogin()+"'";
        ArrayList<Locality> localityList = new ArrayList<Locality>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Locality location = new Locality();
                location.setTimestamp(new Date(cursor.getString(4)));
                location.setLatitude(cursor.getDouble(2));
                location.setLongitude(cursor.getDouble(3));
                localityList.add(location);
            } while (cursor.moveToNext());
        }

        return localityList;
    }

}