package com.example.locationfinderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // Database and table information
    private static final String DATABASE_NAME = "Assignment2";
    private static final String TABLE_NAME = "Locations";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "ADDRESS";
    private static final String COL_3 = "LATITUDE";
    private static final String COL_4 = "LONGITUDE";

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)  // Handles table creation
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT, "
                + COL_3 + " TEXT, "
                + COL_4 + " TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) // Handles upgrades for database
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Adds new locations to the database
    public void insertLocation(String address, String latitude, String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Variable to communicate with the database
        ContentValues values = new ContentValues(); // Used to hold the location data for each column of the table
        // Put each component of the location data with the associated column
        values.put(COL_2, address);
        values.put(COL_3, latitude);
        values.put(COL_4, longitude);

        db.insert(TABLE_NAME, null, values); // insert the location record

        db.close();
    }

    // Updates existing location records in the database
    public void updateLocation(int id, String address, String latitude, String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Variable to communicate with the database
        ContentValues values = new ContentValues(); // Used to hold the location data for each column of the table
        // Put each component of the location data with the associated column
        values.put(COL_2, address);
        values.put(COL_3, latitude);
        values.put(COL_4, longitude);

        db.update(TABLE_NAME, values, COL_1 + " = ?", new String[]{String.valueOf(id)}); // update the location record

        db.close();
    }

    // Deletes existing location records in the database
    public void deleteLocation(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase(); // Variable to communicate with the database

        db.delete(TABLE_NAME, COL_1 + " = ?", new String[]{String.valueOf(id)}); // delete the location record

        db.close();
    }

    // Searches through all location records based on query
    public Cursor searchLocationRecords(String addressQuery)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Variable to communicate with the database

        String query = "SELECT * FROM locations WHERE address LIKE ?";

        return db.rawQuery(query, new String[]{"%" + addressQuery + "%"}); // Perform query to obtain the location records
    }

    // Obtains a location record through its ID
    public Cursor getLocationRecord(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase(); // Variable to communicate with the database

        String query = "SELECT * FROM locations WHERE id=?";

        return db.rawQuery(query, new String[]{String.valueOf(id)}); // Perform query to obtain the location record
    }
}
