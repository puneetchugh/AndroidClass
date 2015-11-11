package com.nyu.cs9033.eta.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_PEOPLE = "people";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRIP_NAME = "trip_name";
    public static final String COLUMN_TRIP_LOC = "trip_loc";
    public static final String COLUMN_TRIP_DATE = "trip_date";
    public static final String COLUMN_PERSON_NAME = "person_name";
    public static final String COLUMN_TRIP_TIME = "trip_time";

    private static final String DATABASE_NAME = "trips.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRIPS + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_TRIP_LOC
            + " text not null," + COLUMN_TRIP_DATE +" text not null," + COLUMN_TRIP_TIME +" text not null,"+COLUMN_TRIP_NAME+" text not null);";

    private static final String DATABASE_PEOPLE_CREATE = "create table "
            + TABLE_PEOPLE + "(" + COLUMN_ID
            + " integer ," + COLUMN_PERSON_NAME+" text key not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_PEOPLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        onCreate(db);
    }

}

