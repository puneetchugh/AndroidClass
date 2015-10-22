package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";

	private TextView firstPersonTextView;
	private TextView secondPersonTextView;
	private TextView thirdPersonTextView;
	private TextView fourthPersonTextView;
	private TextView fifthPersonTextView;
	private TextView dateTextView;
	private TextView timeTextView;
	private TextView locationTextView;
	private TextView tripName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.view_trip);
		firstPersonTextView = (TextView) findViewById(R.id.friend_one_id);
		secondPersonTextView = (TextView) findViewById(R.id.friend_two_id);
		thirdPersonTextView = (TextView) findViewById(R.id.friend_three_id);
		fourthPersonTextView = (TextView) findViewById(R.id.friend_four_id);
		fifthPersonTextView = (TextView) findViewById(R.id.friend_five_id);
		dateTextView = (TextView) findViewById(R.id.view_date_id);
		timeTextView = (TextView) findViewById(R.id.view_time_id);
		locationTextView = (TextView) findViewById(R.id.view_location_id);
		tripName = (TextView) findViewById(R.id.trip_name_id);


		//setContentView(R.layout.view_trip);

		Intent tripIntent = getIntent();
		Trip trip = getTrip(tripIntent, savedInstanceState);
		if(trip == null){
			Toast.makeText(this, "You cannot View the trip before creating one !", Toast.LENGTH_SHORT).show();
			Intent newIntent = new Intent(this, MainActivity.class);
			startActivity(newIntent);
		}
		viewTrip(trip);
	}
	
	/**
	 * Create a Trip object via the recent trip that
	 * was passed to TripViewer via an Intent.
	 * 
	 * @param i The Intent that contains
	 * the most recent trip data.
	 * 
	 * @return The Trip that was most recently
	 * passed to TripViewer, or null if there
	 * is none.
	 */
	public Trip getTrip(Intent i, Bundle newBundle) {
		
		// TODO - fill in here
		//Bundle newBundle = new Bundle();
		newBundle = i.getExtras();
		if(newBundle == null){
			return null;
		}
		Trip trip = (Trip)newBundle.getParcelable("TRIP");

		String location = trip.getLocation();
		String date = trip.getDate();
		String time = trip.getTime();
		String firstName = trip.getFirstPerson();
		String secondName = trip.getSecondPerson();
		String thirdName = trip.getThirdPerson();
		String fourthName = trip.getFourthPerson();
		String fifthName = trip.getFifthPerson();
		String tripName = trip.getTripName();

		trip = new Trip(location,date,time,firstName,secondName,thirdName,fourthName,fifthName,tripName);
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip The Trip model used to
	 * populate the View.
	 */
	public void viewTrip(Trip trip) {
		
		// TODO - fill in here

		if(trip.getFirstPerson() !=null){
			firstPersonTextView.setText(trip.getFirstPerson());
		}

		if(trip.getSecondPerson() != null){
			secondPersonTextView.setText(trip.getSecondPerson());
		}

		if(trip.getThirdPerson() != null){
			thirdPersonTextView.setText(trip.getThirdPerson());
		}

		if(trip.getFourthPerson() != null){
			fourthPersonTextView.setText(trip.getFourthPerson());
		}

		if(trip.getFifthPerson() != null) {
			fifthPersonTextView.setText(trip.getFifthPerson());

		}

		if(trip.getLocation() != null){
			if(trip.getLocation() ==""){
				locationTextView.setText("Not Mentioned");
			}
			else {
				locationTextView.setText(trip.getLocation());
			}
		}



		if(trip.getDate() != null) {
			if(trip.getLocation() == ""){
				dateTextView.setText("Not mentioned");
			}
			else {
				dateTextView.setText(trip.getDate());
			}
		}


		if(trip.getTime()!=null) {
			if (trip.getTime() == "") {
				timeTextView.setText("Not mentioned");
			} else {
				timeTextView.setText(trip.getTime());
			}

		}

		else{
			timeTextView.setText(trip.getTime());
		}

		if(trip.getTripName() != null){
			if(trip.getTripName() == ""){
				tripName.setText("Not mentioned");
			}
			else {
				tripName.setText(trip.getTripName());
			}
		}
	}
}
