package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;

/**
 * Created by puneet on 11/9/15.
 */
public class ShowListItem extends Activity {

    TextView tripNameId;
    TextView peopleGoingOnTheTripId;
    TextView tripLocationId;
    TextView tripDateId;
    TextView tripTimeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_trip);

        tripNameId = (TextView)findViewById(R.id.trip_name_id);
        peopleGoingOnTheTripId = (TextView) findViewById(R.id.peoples_name);
        tripLocationId = (TextView) findViewById(R.id.view_location_id);
        tripDateId = (TextView) findViewById(R.id.view_date_id);
        tripTimeId = (TextView) findViewById(R.id.view_time_id);

        Intent receivedIntent = getIntent();
        String tripName = receivedIntent.getStringExtra("trip_name");
        String peopleOnTheTrip = receivedIntent.getStringExtra("people_on_the_trip");
        String tripLocation = receivedIntent.getStringExtra("trip_location");
        String tripDate = receivedIntent.getStringExtra("trip_date");
        String tripTime = receivedIntent.getStringExtra("trip_time");

        tripNameId.setText(tripName);
        peopleGoingOnTheTripId.setText(peopleOnTheTrip);
        tripLocationId.setText(tripLocation);
        tripDateId.setText(tripDate);
        tripTimeId.setText(tripTime);



    }


}
