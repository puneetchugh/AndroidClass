package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateTripActivity extends Activity {

	private static int tripCount = 0;
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
	private EditText restaurantType;
	private Button createButton;
	private Button cancelButton;
	private EditText tripName;
	private TextView printAddedPerson;
	private String location = new String();

	private TablesDataSource tablesDataSource;
	//Person personPuneet, personHannah, personPranay, personChenxi, personSheryar;
	//private Object View;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO - fill in here
		setContentView(R.layout.create_trip);
		tablesDataSource = new TablesDataSource(this);
		try {
			tablesDataSource.open();
		}
		catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
		people = new ArrayList<Person>();
		tripName = (EditText)findViewById(R.id.type_name_id);
		//forAddingPeople = (EditText)findViewById(R.id.add_name_id);
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
		restaurantType = (EditText) findViewById(R.id.type_res_type);
		tripName = (EditText) findViewById(R.id.type_name_id);
		createButton = (Button) findViewById(R.id.create_button_id);
		printAddedPerson = (TextView) findViewById(R.id.print_added_name_id);
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

		//String location = tripLocation.getText().toString();
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
		/*
		if (forAddingPeople.getText() != null){
			people.add(new Person(forAddingPeople.getText().toString()));
		}*/

		Trip trip = tablesDataSource.createTrip(tripCount++, location, date, time, name, people);
		//Trip trip = new Trip(tripCount,location,date,time,name,people);
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
		//intent.putExtra(TRIP_DATA,trip);
		setResult(RESULT_CODE, intent);
		finish();

		return true;
		//return false;
	}

	public void addPerson(View view) {
		/*if (forAddingPeople.getText() == null || (forAddingPeople.getText().toString().equals("")) ){
			Toast.makeText(getApplicationContext(), "You did not enter any name",
					Toast.LENGTH_LONG).show();
		} else {
			people.add(new Person(forAddingPeople.getText().toString()));
			forAddingPeople.setText(null);
			forAddingPeople.setHint("Type Name");
			forAddingPeople.setHintTextColor(getResources().getColor(R.color.white));
		}*/
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 5);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in intent : ", e.toString());
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

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if(reqCode == 5) {
			try {
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor cur = managedQuery(contactData, null, null, null, null);
					ContentResolver contect_resolver = getContentResolver();

					if (cur.moveToFirst()) {
						String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
						String name = "";
						String no = "";

						Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

						if (phoneCur.moveToFirst()) {
							name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
							no = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							people.add(new Person(tripCount, name));
						}

						Log.e("Phone no & name :***: ", name + " : " + no);
						printAddedPerson.setText("Added " + name + " : " + no + "\n");

						id = null;
						name = null;
						no = null;
						phoneCur = null;
					}
					contect_resolver = null;
					cur = null;
					//                      populateContacts();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Log.e("IllegalArgumentExcp", e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Error :: ", e.toString());
			}
		}

		else if(reqCode == 1){
			Intent fourSquareReturn = data;
			ArrayList<String> dataFromFourSquare = fourSquareReturn.getStringArrayListExtra("retVal");
			String latitude = dataFromFourSquare.get(2);
			String longitude = dataFromFourSquare.get(3);
			location = dataFromFourSquare.get(0)+" "+dataFromFourSquare.get(1) ;
		}
	}

	public void searchRestaurant(View view){

		String tripLocationString = tripLocation.getText().toString();
		String enterRestaurantString = restaurantType.getText().toString();

		String toBeSent = tripLocationString+"::"+enterRestaurantString;

		Intent fourSquareApi = new Intent(Intent.ACTION_VIEW);
		fourSquareApi.setData(Uri.parse("location://com.example.nyu.hw3api"));
		fourSquareApi.putExtra("searchVal", toBeSent);
		startActivityForResult(fourSquareApi, 1);

	}

}
