package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;

public class CreateTripActivity extends Activity {
	
	private static final String TAG = "CreateTripActivity";
	private static final String MAIN_TAG = "MainActivity";
	private static final String TRIP_DATA = "TripData";
	private static final int RESULT_CODE = 2;
	private int numberOfPeople;
	ArrayList<Person> people;
	private Intent intent;
	/*
	private CheckBox puneet;
	private CheckBox hannah;
	private CheckBox pranay;
	private CheckBox chenxi;
	private CheckBox sheryar;
	*/
	private EditText forAddingPeople;
	private EditText tripDate;
	private EditText tripTime;
	private EditText tripLocation;
	private Button createButton;
	private Button cancelButton;
	private EditText tripName;

	//Person personPuneet, personHannah, personPranay, personChenxi, personSheryar;
	//private Object View;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.create_trip);
		people = new ArrayList<Person>();
		tripName = (EditText)findViewById(R.id.type_name_id);
		forAddingPeople = (EditText)findViewById(R.id.add_name_id);
		/*
		puneet = (CheckBox) findViewById(R.id.puneet_id);
		hannah = (CheckBox) findViewById(R.id.hannah_id);
		pranay = (CheckBox) findViewById(R.id.pranay_id);
		chenxi = (CheckBox) findViewById(R.id.chenxi_id);
		sheryar = (CheckBox) findViewById(R.id.sheryar_id);
		*/
		tripDate = (EditText) findViewById(R.id.date_id);
		tripTime = (EditText) findViewById(R.id.time_id);
		tripLocation = (EditText) findViewById(R.id.location_id);
		tripName = (EditText) findViewById(R.id.type_name_id);
		createButton = (Button) findViewById(R.id.create_button_id);
		createButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View view) {


				Trip trip = createTrip();


				saveTrip(trip);


			}

		});

	}

	/**
	 * This method should be used to
	 * instantiate a Trip model object.
	 * 
	 * @return The Trip as represented
	 * by the View.
	 */
	public Trip createTrip() {


		// TODO - fill in here

		String location = tripLocation.getText().toString();
		String date = tripDate.getText().toString();
		String time = tripTime.getText().toString();
		String name = tripName.getText().toString();


		/*
		if(puneet.isChecked()){
			personPuneet = new Person("Puneet");
		}
		else{
			personPuneet = new Person("");
		}

		if(hannah.isChecked()){
			personHannah = new Person("Hannah");
		}
		else{
			personHannah = new Person("");
		}

		if(pranay.isChecked()){
			personPranay = new Person("Pranay");
		}
		else{
			personPranay = new Person("");
		}

		if(chenxi.isChecked()){
			personChenxi = new Person("Chenxi");
		}
		else{
			personChenxi = new Person("");
		}

		if(sheryar.isChecked()){
			personSheryar = new Person("Sheryar");
		}
		else{
			personSheryar = new Person("");
		}
		*/
		if (forAddingPeople.getText() != null){
			people.add(new Person(forAddingPeople.getText().toString()));
		}


		Trip trip = new Trip(location,date,time,name,people);
		return trip;
		//return null;
	}

	/**
	 * For HW2 you should treat this method as a 
	 * way of sending the Trip data back to the
	 * main Activity.
	 * 
	 * Note: If you call finish() here the Activity 
	 * will end and pass an Intent back to the
	 * previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully 
	 * saved.
	 */
	public boolean saveTrip(Trip trip) {
	
		// TODO - fill in here


		Intent intent = new Intent(CreateTripActivity.this, MainActivity.class);
		//Bundle newBundle = new Bundle();
		//newBundle.putParcelable(TRIP_DATA,trip);
		//intent.putExtras(newBundle);
		intent.putExtra(TRIP_DATA,trip);
		setResult(RESULT_CODE, intent);
		finish();

		return true;
		//return false;
	}

	public void addPerson(View view) {
		if (forAddingPeople.getText() == null || (forAddingPeople.getText().toString().equals("")) ){
			Toast.makeText(getApplicationContext(), "You did not enter any name",
					Toast.LENGTH_LONG).show();
		} else {
			people.add(new Person(forAddingPeople.getText().toString()));
			forAddingPeople.setText(null);
			forAddingPeople.setHint("Type Name");
			forAddingPeople.setHintTextColor(getResources().getColor(R.color.white));
		}
	}
	/**
	 * This method should be used when a
	 * user wants to cancel the creation of
	 * a Trip.
	 * 
	 * Note: You most likely want to call this
	 * if your activity dies during the process
	 * of a trip creation or if a cancel/back
	 * button event occurs. Should return to
	 * the previous activity without a result
	 * using finish() and setResult().
	 */
	public void cancelTrip() {

		finish();
		// TODO - fill in here
	}


}
