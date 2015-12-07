package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateTripActivity extends Activity {

	//private static int tripCount = 0;
	private static final String TAG = "CreateTripActivity";
	private static final String MAIN_TAG = "MainActivity";
	private static final String TRIP_DATA = "TripData";
	private static final int RESULT_CODE = 2;
	private int numberOfPeople;
	ArrayList<Person> people;
	ArrayList<String> peopleNamesTemp = new ArrayList<String>();
	private Intent intent;
	private Trip trip;

	private static long tripId;
	private String loc_latitude;
	private String loc_longitude;
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
	private String date;
	private String time;
	private String name;
	private TablesDataSource tablesDataSource;
	JSONObject jsonObject = new JSONObject();

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

		date = tripDate.getText().toString();
		if(date == null || date.matches("")){
			Toast.makeText(this, "Missing some field. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
			return null;
		}
		try {

			String[] dateStringsCombined = date.split("-");
			int[] dateStrings = new int[3];
			dateStrings[0] = Integer.parseInt(dateStringsCombined[0]);
			dateStrings[1] = Integer.parseInt(dateStringsCombined[1]);
			dateStrings[2] = Integer.parseInt(dateStringsCombined[2]);
			if((dateStrings[0]>1000) && (dateStrings[0] <10000) && (dateStrings[1] > 0) && (dateStrings[1] <= 12) && (dateStrings[2]> 0) && (dateStrings[2] <= 31)){
			}
			else{
				Toast.makeText(this, "You did not enter the date in yyyy-MM-dd format. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
				return null;

			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		}catch (Exception e){
			Toast.makeText(this, "You did not enter the date in dd.MM.yyyy format. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
		}
		time = tripTime.getText().toString();

		if(time == null || time.matches("")){
			Toast.makeText(this, "Missing some field. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
		}
		try{
			String[] timeValidation;
			timeValidation = time.split(":");
			int counter = 0;
			//for(String timeVal: timeValidation){
			int timeValIntegerHour = Integer.parseInt(timeValidation[0]);
			if((timeValIntegerHour >24) || (timeValIntegerHour<0)){
				Toast.makeText(this,"You did not enter a valid time in HH:MM format. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
				return null;
				}
			int timeValIntegerMinute = Integer.parseInt(timeValidation[1]);
			if((timeValIntegerMinute > 60) || (timeValIntegerMinute < 0)){
				Toast.makeText(this, "You did not enter a valid time in HH:MM format. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
				return null;
			}
			}catch (Exception e) {
			Toast.makeText(this, "You did not enter a valid time in HH:MM format. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
			return null;
		}

		name = tripName.getText().toString();
		if(name == null || name.matches("")){
			Toast.makeText(this,"Missing some field. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
			return null;
		}

		if(location == null || location.matches("")){
			Toast.makeText(this,"Missing some field. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
			return null;
		}


		JSONArray jsonArray = new JSONArray();
		jsonArray.put(name);
		jsonArray.put(location);
		jsonArray.put(loc_latitude);
		jsonArray.put(loc_longitude);
		try {
			jsonObject.put("command", "CREATE_TRIP");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			jsonObject.put("location", jsonArray );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DateFormat sdf = new SimpleDateFormat(date+" "+time+":00.000");
		Date dateTime = null;
		try {
			dateTime = sdf.parse(date+" "+time+":00.000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeInMillisSinceEpoch = dateTime.getTime();
		//long timeInMinutesSinceEpoch = timeInMillisSinceEpoch / (60 * 1000);
		try {
			jsonObject.put("datetime",timeInMillisSinceEpoch);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//jsonObject.put("datetime",382584629);
		JSONArray peopleJSONArray = new JSONArray();
		for(String individual: peopleNamesTemp){
			peopleJSONArray.put(individual);
		}
		//peopleJSONArray.put("John Doe");
		//peopleJSONArray.put("Joe Smith");
		try {
			jsonObject.put("people", peopleJSONArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		makeTripHttpPostRequest();




/*
		new CountDownTimer(5000, 1000) {
			public void onFinish() {
				// When timer is finished
				// Execute your code here
				trip = tablesDataSource.createTrip(tripId, location, date, time, name, people, loc_latitude, loc_longitude);


			}

			public void onTick(long millisUntilFinished) {
				// millisUntilFinished    The amount of time until finished.
			}
		}.start();
*/
		/*
		try {
			Thread.sleep(2000);
		}catch (InterruptedException e){

			e.printStackTrace();
		}
*/
		SystemClock.sleep(1000);

		for(String oneName: peopleNamesTemp){
			people.add(new Person(tripId, oneName));
		}

		if((people == null) || (people.size() == 0)){
			Toast.makeText(this,"Missing some field. TRIP NOT SAVED", Toast.LENGTH_SHORT).show();
			return null;
		}

		trip = tablesDataSource.createTrip(tripId, location, date, time, name, people, loc_latitude, loc_longitude);


		return trip;



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

		finish();

		return true;

	}

	public void addPerson(View view) {

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
	public void cancelTrip(View view) {

		finish();

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
							//people.add(new Person(tripId, name));
							peopleNamesTemp.add(name);
						}

						Log.e("Phone no & name :***: ", name + " : " + no);
						printAddedPerson.setText("Added " + name + " : " + no + "\n");
						phoneCur.close();

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
			loc_latitude = dataFromFourSquare.get(2);
			loc_longitude = dataFromFourSquare.get(3);
			location = dataFromFourSquare.get(0)+" "+dataFromFourSquare.get(1) ;
		}
	}

	public void searchRestaurant(View view){

		String tripLocationString = tripLocation.getText().toString();
		String enterRestaurantString = restaurantType.getText().toString();
		//Toast.makeText(this, "tripLocaiton is "+ tripLocationString, Toast.LENGTH_SHORT).show();

		if((tripLocationString == null)|| (tripLocationString.matches("")) || (enterRestaurantString == null) || (enterRestaurantString.matches(""))){
			Toast.makeText(this, "You have to enter both fields for restaurant", Toast.LENGTH_SHORT).show();
			return;
		}
		String toBeSent = tripLocationString+"::"+enterRestaurantString;

		Intent fourSquareApi = new Intent(Intent.ACTION_VIEW);
		fourSquareApi.setData(Uri.parse("location://com.example.nyu.hw3api"));
		fourSquareApi.putExtra("searchVal", toBeSent);
		startActivityForResult(fourSquareApi, 1);

	}


	@Override
	protected void onResume() {
		try {
			tablesDataSource.open();
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		tablesDataSource.close();
		super.onPause();
	}


	private void response(String someResponse){

		long[] responseData = new long[2];

		try {
			responseData = getDataFromJson(someResponse);
		}
		catch (JSONException e) {
			e.printStackTrace();
			//productInfo.setText(e.getMessage());// set productInfo toast or message
		}


		tripId = responseData[1];
		//tripInfo.setText("ResponseCode = " + Integer.toString(responseData[0]) + "\nTrip Id = " + Integer.toString(responseData[1]));


	}

	public static long[] getDataFromJson(String jString) throws JSONException {

		ArrayList<String> people = new ArrayList<String>();

		JSONObject myjson = new JSONObject(jString);


		int responseCode = myjson.getInt("response_code");

		tripId = myjson.getInt("trip_id");


		long[] response = new long[2];
		response[0] = responseCode;
		response[1] = tripId;

		return response;

	}

	private class CallAPI extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			InputStream in = null;
			int resCode = -1;
			try {
				URL url = new URL(params[0]);
				URLConnection urlConn = url.openConnection();

				if (!(urlConn instanceof HttpURLConnection)) {
					throw new IOException("URL is not an Http URL");
				}
				HttpURLConnection httpConn = (HttpURLConnection) urlConn;
				httpConn.setAllowUserInteraction(false);
				httpConn.setInstanceFollowRedirects(true);
				httpConn.setRequestMethod("POST");
				httpConn.connect();


				OutputStreamWriter out = new   OutputStreamWriter(httpConn.getOutputStream());
				out.write(jsonObject.toString());
				out.close();
				resCode = httpConn.getResponseCode();

				if (resCode == HttpURLConnection.HTTP_OK) {
					in = httpConn.getInputStream();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String json = convertStreamToString(in);

			long[] jsonData = new long[2];
			try {
				jsonData = getDataFromJson(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			//JSONObject myjson = new JSONObject(json);
			tripId = jsonData[1];



			return json;

		}


		public String convertStreamToString(InputStream is){
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;

			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
			} finally {
				try {
					is.close();
				} catch (IOException e) {
				}
			}

			return sb.toString();
		}
		protected void onPostExecute(String stream_url) {
			super.onPostExecute(stream_url);
			//response(stream_url);
		}
	}

	public void makeTripHttpPostRequest() {

		String urlstring = "http://cs9033-homework.appspot.com/";

		new CallAPI().execute(urlstring);
	}
}
