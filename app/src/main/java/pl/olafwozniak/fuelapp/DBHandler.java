package pl.olafwozniak.fuelapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    FuelLab mFuelLab;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CrimeDB.db";

    public static final String TABLE_NAME = "crimes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CRIME_ID = "crime_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mFuelLab = FuelLab.get(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("[INFO] Initialized empty crime database.");
        String INITIALIZE_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, COLUMN_ID, COLUMN_CRIME_ID, COLUMN_TITLE, COLUMN_DATE);
        db.execSQL(INITIALIZE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(db);
    }

    public Cursor getFuels() {
        String q = String.format("SELECT * FROM %s", TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(q, null);
    }

    public Cursor getFuels(int id) {
        String uuidString = Integer.toString(id);
        String q = String.format("SELECT * FROM %s WHERE = ?", TABLE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(q, new String[]{uuidString});
    }

    public void addFuel(Fuel fuel) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_CRIME_ID, FuelLab.mFuels.size());
        v.put(COLUMN_TITLE, "Other");
        v.put(COLUMN_DATE, new Date().toString());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, v);
        db.close();
        mFuelLab.newFuel(fuel);
    }

    public void deleteFuel(int id) {
        String uuidString = Integer.toString(id);
        String q = String.format("SELECT * FROM %s WHERE %s = %s", TABLE_NAME, COLUMN_CRIME_ID, uuidString);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CRIME_ID + " = ?", new String[]{String.valueOf(uuidString)});
        db.close();
        mFuelLab.deleteFuel(id);
    }

    public void updateFuel(int id, String title, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, date.toString());
        db.update(TABLE_NAME, cv, COLUMN_CRIME_ID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }
}

