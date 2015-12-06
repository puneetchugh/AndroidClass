package com.nyu.cs9033.eta.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by puneet on 11/7/15.
 */
public class TablesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsTripTable = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TRIP_LOC, MySQLiteHelper.COLUMN_TRIP_DATE, MySQLiteHelper.COLUMN_TRIP_TIME,
            MySQLiteHelper.COLUMN_TRIP_NAME, MySQLiteHelper.COLUMN_TRIP_LOC_LATTITUDE, MySQLiteHelper.COLUMN_TRIP_LOC_LONGITUDE };

    private String[] allColumnsPeopleTable = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PERSON_NAME};

    public TablesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Trip createTrip(int id, String tripLocation, String tripDate, String tripTime, String tripName, ArrayList<Person> people, String loc_latitude, String loc_longitude) {
        ContentValues values1 = new ContentValues();
        values1.put(MySQLiteHelper.COLUMN_ID, id);
        values1.put(MySQLiteHelper.COLUMN_TRIP_LOC, tripLocation);
        values1.put(MySQLiteHelper.COLUMN_TRIP_DATE, tripDate);
        values1.put(MySQLiteHelper.COLUMN_TRIP_TIME, tripTime);
        values1.put(MySQLiteHelper.COLUMN_TRIP_NAME, tripName);
        values1.put(MySQLiteHelper.COLUMN_TRIP_LOC_LATTITUDE, loc_latitude);
        values1.put(MySQLiteHelper.COLUMN_TRIP_LOC_LONGITUDE, loc_longitude);
        long insertId = database.insert(MySQLiteHelper.TABLE_TRIPS, null,
                values1);
        Cursor cursor1 = database.query(MySQLiteHelper.TABLE_TRIPS,
                allColumnsTripTable, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor1.moveToFirst();


        ContentValues values2 = new ContentValues();
        values2.put(MySQLiteHelper.COLUMN_ID, id);
        for (Person newPerson : people) {
            values2.put(MySQLiteHelper.COLUMN_ID, id);
            values2.put(MySQLiteHelper.COLUMN_PERSON_NAME, newPerson.getName());
            database.insert(MySQLiteHelper.TABLE_PEOPLE, null, values2);


        }

        Cursor cursor2 = database.query(MySQLiteHelper.TABLE_PEOPLE,
                allColumnsPeopleTable, MySQLiteHelper.COLUMN_ID + "=" + id, null,
                null, null, null);
        cursor2.moveToFirst();

        Trip newTrip = cursorToTrip(cursor1, cursor2);
        cursor1.close();
        cursor2.close();
        return newTrip;
    }


    public void deleteTRip(Trip trip) {
        int tripId = trip.getId();
        System.out.println("Trip deleted with id: " + tripId);
        database.delete(MySQLiteHelper.TABLE_TRIPS, MySQLiteHelper.COLUMN_ID
                + " = " + tripId, null);

        database.delete(MySQLiteHelper.TABLE_PEOPLE, MySQLiteHelper.COLUMN_ID
                + "=" + tripId, null);
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<Trip>();

        Cursor cursor1 = database.query(MySQLiteHelper.TABLE_TRIPS,
                allColumnsTripTable, null, null, null, null, null);

        Cursor cursor2 = database.query(MySQLiteHelper.TABLE_PEOPLE,
                allColumnsPeopleTable, null, null, null, null, null);

        cursor2.moveToFirst();
        cursor1.moveToFirst();
        while (!cursor1.isAfterLast()) {
            Trip trip = cursorToTrip(cursor1, cursor2);
            trips.add(trip);
            cursor1.moveToNext();

        }
        cursor1.close();
        cursor2.close();
        return trips;
    }

    private Trip cursorToTrip(Cursor cursor1, Cursor cursor2) {
        int tripId = cursor1.getInt(0);
        ArrayList<Person> peopleArrayList = new ArrayList<Person>();
        String tripLocation = cursor1.getString(1);
        String tripDate = cursor1.getString(2);
        String tripTime = cursor1.getString(3);
        String tripName = cursor1.getString(4);
        peopleArrayList = cursorToPerson(tripId, cursor2);
        String loc_latitude = cursor1.getString(5);
        String loc_longitude = cursor1.getString(6);
        Trip trip= new Trip(tripId, tripLocation, tripDate, tripTime, tripName, peopleArrayList, loc_latitude, loc_longitude);

        return trip;
    }

    private ArrayList<Person> cursorToPerson(int tripId, Cursor cursor){
        ArrayList<Person> arrayList = new ArrayList<Person>();
        //int tripId = cursor.getInt(0);
        while (!cursor.isAfterLast()){

            if(cursor.getInt(0) == tripId) {

                String name = cursor.getString(1);
                arrayList.add(new Person(tripId, name));
                cursor.moveToNext();
            }

            else{
                break;
            }
        }
        return arrayList;
    }
}
