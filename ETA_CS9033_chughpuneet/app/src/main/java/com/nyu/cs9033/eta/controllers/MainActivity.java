package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String CREATE_TAG = "CreateTripActivity";
	private static final String TRIP_DATA = "TripData";
	private Intent createTripIntent;
	private Intent viewTripIntent;


	Bundle newBundle = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	}

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {


		createTripIntent = new Intent(this, CreateTripActivity.class);
		startActivity(createTripIntent);
	}

	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startViewTripActivity(View view) {

		TablesDataSource tablesDataSource = new TablesDataSource(this);
		try {
			tablesDataSource.open();
		} catch (SQLException sqlException) {
			Toast.makeText(this, "You cannot view the trips before creating one", Toast.LENGTH_SHORT).show();
			sqlException.printStackTrace();
		}
		List<Trip> tripArrayList = tablesDataSource.getAllTrips();
		if (tripArrayList.size() == 0) {
			Toast.makeText(this, "You cannot view the trips before creating one", Toast.LENGTH_SHORT).show();
		} else {
			viewTripIntent = new Intent(this, ViewTripActivity.class);

			startActivity(viewTripIntent);
		}
	}




}