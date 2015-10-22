package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String CREATE_TAG = "CreateTripActivity";
	private static final String TRIP_DATA = "TripData";
	private Intent createTripIntent;
	private Intent viewTripIntent;
	private Trip trip;
	Parcel tripParcel;

	Bundle newBundle = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO - fill in here


	}

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {

		// TODO - fill in here
		createTripIntent = new Intent(this, CreateTripActivity.class);
		startActivityForResult(createTripIntent, 1);

	}

	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startViewTripActivity(View view) {

		// TODO - fill in here
		if(trip == null){
			Toast.makeText(this, "You cannot View the trip before creating one !", Toast.LENGTH_SHORT).show();
		}
		else {
			Bundle newBundle = new Bundle();
			viewTripIntent = new Intent(this, ViewTripActivity.class);
			newBundle.putParcelable("TRIP", trip);
			viewTripIntent.putExtras(newBundle);
			startActivity(viewTripIntent);
		}
	}

	/**
	 * Receive result from CreateTripActivity here.
	 * Can be used to save instance of Trip object
	 * which can be viewed in the ViewTripActivity.
	 * <p/>
	 * Note: This method will be called when a Trip
	 * object is returned to the main activity.
	 * Remember that the Trip will not be returned as
	 * a Trip object; it will be in the persisted
	 * Parcelable form. The actual Trip object should
	 * be created and saved in a variable for future
	 * use, i.e. to view the trip.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO - fill in here
		Bundle newBundle = new Bundle();
		if (resultCode == 2) {
			if (data != null) {

				newBundle = data.getExtras();
				if(newBundle != null){
					trip = (Trip)newBundle.getParcelable("TripData");
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

					//trip = new Trip(parcel);
				//trip = (Trip)getIntent().getExtras("TripData");



				}
			}
		}
	}
}
