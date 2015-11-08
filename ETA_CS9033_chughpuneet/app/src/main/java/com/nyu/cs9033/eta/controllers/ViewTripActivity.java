package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.sql.SQLException;
import java.util.ArrayList;

public class ViewTripActivity extends Activity {

	private TablesDataSource tablesDataSource;
	private static final String TAG = "ViewTripActivity";
	private ArrayList<TextView> displayPeople;
	private TextView gettingTripName;
	/*
	private TextView firstPersonTextView;
	private TextView secondPersonTextView;
	private TextView thirdPersonTextView;
	private TextView fourthPersonTextView;
	private TextView fifthPersonTextView;
	*/
	private TextView dateTextView;
	private TextView timeTextView;
	private TextView locationTextView;
	private TextView tripName;
	private TextView peopleOnTheTrip;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.view_trip);
		displayPeople = new ArrayList<TextView>();
		try {
			tablesDataSource.open();
		}catch (SQLException sqlException ){
			sqlException.printStackTrace();
		}

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.view_trip_id);
		/*
		firstPersonTextView = (TextView) findViewById(R.id.friend_one_id);
		secondPersonTextView = (TextView) findViewById(R.id.friend_two_id);
		thirdPersonTextView = (TextView) findViewById(R.id.friend_three_id);
		fourthPersonTextView = (TextView) findViewById(R.id.friend_four_id);
		fifthPersonTextView = (TextView) findViewById(R.id.friend_five_id);
		*/
		dateTextView = (TextView) findViewById(R.id.view_date_id);
		timeTextView = (TextView) findViewById(R.id.view_time_id);
		locationTextView = (TextView) findViewById(R.id.view_location_id);
		gettingTripName = (TextView) findViewById(R.id.trip_name_id);
		peopleOnTheTrip = (TextView) findViewById(R.id.people_on_trip_id);
		tripName = (TextView) findViewById(R.id.trip_name);
		peopleOnTheTrip = (TextView) findViewById(R.id.peoples_name);


		//setContentView(R.layout.view_trip);

		Intent tripIntent = getIntent();
		Trip trip = getTrip(tripIntent, savedInstanceState);
		if(trip == null){
			Toast.makeText(this, "You cannot View the trip before creating one !", Toast.LENGTH_SHORT).show();
			Intent newIntent = new Intent(this, MainActivity.class);
			startActivity(newIntent);
		}
		for(int loopCounter = 0; loopCounter < trip.getNumberOfPerson(); loopCounter++){
			displayPeople.add(new TextView(this));
			displayPeople.get(loopCounter).setId(loopCounter);
			linearLayout.addView(displayPeople.get(loopCounter));
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
		/*
		String firstName = trip.getFirstPerson();
		String secondName = trip.getSecondPerson();
		String thirdName = trip.getThirdPerson();
		String fourthName = trip.getFourthPerson();
		String fifthName = trip.getFifthPerson();
		*/
		String tripName = trip.getTripName();
		int numberOfPeople = trip.getNumberOfPerson();
		ArrayList<String> peopleString = trip.getNames();


		ArrayList<Person> people = new ArrayList<Person>();
		for(int loopCounter = 0; loopCounter < numberOfPeople; loopCounter++){
			people.add(new Person(peopleString.get(tripId, loopCounter)));
		}

		trip = new Trip(tripId, location,date,time,tripName, people);
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
		/*
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
		*/

		ArrayList<String> people = trip.getNames();
		int loopCounter = 0;
		StringBuilder peopleStringBuilder = new StringBuilder();
		for(String individual: people){
			peopleStringBuilder.append(individual);
			peopleStringBuilder.append("; ");
		}
		String peopleString = peopleStringBuilder.toString();
		peopleOnTheTrip.setText(peopleString);
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
				gettingTripName.setText("Not mentioned");
			}
			else {
				gettingTripName.setText(trip.getTripName());
			}
		}
	}
}
