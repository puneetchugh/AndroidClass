package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String CREATE_TAG = "CreateTripActivity";
	private static final String TRIP_DATA = "TripData";
	private static final int TEN_SECONDS = 10000;
	private static final int SLEEP_TIME = 100;
	private static final String PEOPLE = "people";
	private static final String TIME_LEFT = "time_left";
	private static final String DISTANCE_LEFT = "distance_left";
	private static final String TRIP_STATUS = "TRIP_STATUS";
	private static final String UPDATE_LOCATION = "UPDATE_LOCATION";



	private Intent createTripIntent;
	private Intent viewTripIntent;

	private TextView personOneName;
	private TextView personOneDistance;
	private TextView personOneTime;
	private TextView personTwoName;
	private TextView personTwoDistance;
	private TextView personTwoTime;

	private TextView displayOnGoingTrip;
	private TextView displayTripName;
	private TextView displayTripLocation;
	private TextView person1Details;
	private TextView person2Details;
	private String latString = "0.0";
	private String longString = "0.0";
	private Person singlePerson = null;
	private ArrayList<Person> allPeople = new ArrayList<Person>();

	//
	// private
	private static Trip onGoingTrip = null;
	TablesDataSource tablesDataSource;

	Bundle newBundle = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		displayOnGoingTrip = (TextView) findViewById(R.id.on_going_trip);

		displayTripName = (TextView) findViewById(R.id.display_trip_name);
		displayTripLocation = (TextView) findViewById(R.id.display_trip_location);

		personOneName = (TextView) findViewById(R.id.person_one_name);
		personOneDistance = (TextView) findViewById(R.id.person_one_distance);
		personOneTime = (TextView) findViewById(R.id.person_one_time);

		personTwoName = (TextView) findViewById(R.id.person_two_name);
		personTwoDistance = (TextView) findViewById(R.id.person_two_distance);
		personTwoTime = (TextView) findViewById(R.id.person_two_time);

		tablesDataSource = new TablesDataSource(this);
		try {
			tablesDataSource.open();
		}
		catch (SQLException sqlException){
			sqlException.printStackTrace();
		}

		Intent intent = getIntent();
		new TimeChecker().execute();


	}


	public long dateTime(Trip trip){

		//DateTime dateTimeInUtc = new DateTime( "2011-07-19T18:23:20+0000", DateTimeZone.UTC );

		//DateFormat df = new SimpleDateFormat(trip.getDate()+" "+trip.getTime()+":00 UTC");
		Date dateTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ZZZ");

			//String date = trip.getDate();
			//String time = trip.getTime();
			String dateTimeString = trip.getDate()+" "+trip.getTime()+":00 UTC";
			dateTime = formatter.parse(dateTimeString);


		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeInMillisSinceEpoch = dateTime.getTime();

		return timeInMillisSinceEpoch;
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

	private String modifyDateLayout(String inputDate) throws ParseException{
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
	}

	public void updateLocation(long tripId){

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		Double lat,lon;

		try {
			lat = location.getLatitude();
			lon = location.getLongitude();
			latString = Double.toString(lat);
			longString = Double.toString(lon);
			makeTripHttpPostRequest();
			SystemClock.sleep(500);
			tablesDataSource.updatePersonLocation(tripId, latString, longString);
		}

		catch (NullPointerException e){
			e.printStackTrace();
		}

	}





	private class TimeChecker extends AsyncTask<String, String, String>{


		@Override
		protected String doInBackground(String... params){


			if(getIntent().getExtras() != null){

				String trip_id = getIntent().getStringExtra("trip_id");
				onGoingTrip = tablesDataSource.getTrip(Long.parseLong(trip_id));
				long tripTimeEpoc = dateTime(onGoingTrip);
				long presentEpocTime = System.currentTimeMillis();
				if(tripTimeEpoc > presentEpocTime){

					updateUI();
					allPeople = tablesDataSource.getAllPeople(Long.parseLong(trip_id));
					while(tripTimeEpoc > System.currentTimeMillis()){

					if((System.currentTimeMillis() >= tripTimeEpoc)){

						timeOverUIDisplay();
						SystemClock.sleep(100);
					}

					if((System.currentTimeMillis() % TEN_SECONDS) == 0){

					makeTripHttpPostRequestStatus();


					for(int loopCounter = 0; loopCounter < allPeople.size(); loopCounter++){

					singlePerson = allPeople.get(loopCounter);
					makeTripHttpPostRequest();
			//
				// SystemClock.sleep(SLEEP_TIME);
			}
			//getTripStatus();

			//onGoingTrip = tablesDataSource.getTrip(Long.parseLong(trip_id));
									}
								}
							}

			else{
				tripInThePastUIDisplay();
			}
						}


			return null;


		}

		private void tripInThePastUIDisplay() {

			displayOnGoingTrip.setText("You cannot start this trip. The time of this trip is earlier than the present time");
			displayOnGoingTrip.setBackgroundResource(R.color.gray);
		}

		public void timeOverUIDisplay(){

			displayOnGoingTrip.setText("Enjoy your timeout !!");
			displayOnGoingTrip.setBackgroundResource(R.color.gray);

		}


		public void updateUI(){
			displayOnGoingTrip.setText(" On Going Trip");
			displayOnGoingTrip.setBackgroundResource(R.color.gray);
			displayTripName.setBackgroundResource(R.color.gray);
			displayTripLocation.setBackgroundResource(R.color.gray);
			displayTripName.setText(onGoingTrip.getTripName());
			displayTripLocation.setText(onGoingTrip.getLocation());

		}

		public void clearUI(){
			displayOnGoingTrip.setText("");
			displayOnGoingTrip.setBackgroundResource(R.color.transparent);
			displayTripName.setBackgroundResource(R.color.transparent);
			displayTripLocation.setBackgroundResource(R.color.transparent);
			displayTripName.setText("");
			displayTripLocation.setText("");

			personOneName.setBackgroundResource(R.color.transparent);
			personOneName.setText("");
			personOneDistance.setBackgroundResource(R.color.transparent);
			personOneDistance.setText("");
			personOneTime.setBackgroundResource(R.color.transparent);
			personOneTime.setText("");

			personTwoName.setBackgroundResource(R.color.transparent);
			personTwoName.setText("");
			personTwoDistance.setBackgroundResource(R.color.transparent);
			personTwoDistance.setText("");
			personTwoTime.setBackgroundResource(R.color.transparent);
			personTwoTime.setText("");
		}
		protected void onPostExecute(String stream_url) {
			super.onPostExecute(stream_url);

			clearUI();

		}
	}


	private class CallHttpStatus extends AsyncTask<String, String, String> {

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
				try {
					JSONObject jsonObject = new JSONObject();

					jsonObject.put("command", TRIP_STATUS);

					jsonObject.put("trip_id", onGoingTrip.getId());

					OutputStreamWriter out = new   OutputStreamWriter(httpConn.getOutputStream());
					out.write(jsonObject.toString());
					out.close();
				}catch (JSONException je){

					je.printStackTrace();
				}
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

			long jsonData = 1111;
			try {
				 getDataFromJson(json);

			} catch (JSONException e) {
				e.printStackTrace();
			}


			return json;

		}

		public void getDataFromJson(String jString) throws JSONException {

			//ArrayList<String> people = new ArrayList<String>();

			JSONObject myjson = new JSONObject(jString);


			JSONArray peopleJSON = myjson.getJSONArray(PEOPLE);
			JSONArray distanceLeftJSON = myjson.getJSONArray(DISTANCE_LEFT);
			JSONArray timeLeftJSON = myjson.getJSONArray(TIME_LEFT);

			String[] people = new String[2];
			int counter = 0;
			for(;counter < 2; counter++){
					people[counter] = peopleJSON.getString(counter);
			}

			String[] distanceLeft = new String[2];

			for(counter = 0;counter < 2; counter++){
				distanceLeft[counter] = distanceLeftJSON.getString(counter);
			}

			String[] timeLeft = new String[2];
			for(counter = 0; counter < 2; counter++){
				timeLeft[counter] = timeLeftJSON.getString(counter);
			}

			tripStatusDisplay(people, distanceLeft, timeLeft);
			//tripId = myjson.getInt("trip_id");


			//long[] response = new long[2];
			//response[0] = responseCode;
			//response[1] = tripId;

			//return responseCode;

		}

		public void tripStatusDisplay(String[] people, String[] distanceLeft, String[] timeLeft){

			personOneName.setBackgroundResource(R.color.gray);
			personOneName.setText("Person 1 : "+people[0]);
			personOneDistance.setBackgroundResource(R.color.gray);
			personOneDistance.setText("Person 1 distance: "+distanceLeft[0]);
			personOneTime.setBackgroundResource(R.color.gray);
			personOneTime.setText("Person 1 Time-left: "+timeLeft[0]);

			personTwoName.setBackgroundResource(R.color.gray);
			personTwoName.setText("Person 1 : " + people[1]);
			personTwoDistance.setBackgroundResource(R.color.gray);
			personTwoDistance.setText("Person 1 distance: " + distanceLeft[1]);
			personTwoTime.setBackgroundResource(R.color.gray);
			personTwoTime.setText("Person 1 Time-left: "+timeLeft[1]);
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

	public void makeTripHttpPostRequestStatus() {

		String urlstring = "http://cs9033-homework.appspot.com/";

		new CallHttpStatus().execute(urlstring);

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
				try {
					JSONObject jsonObject = new JSONObject();
					/*JSONArray jsonArray = new JSONArray();
					jsonArray.put("location name");
					jsonArray.put("address name");
					jsonArray.put("latitude");
					jsonArray.put("longitude");*/
					jsonObject.put("command", UPDATE_LOCATION);
					//jsonObject.put("location", jsonArray );
					jsonObject.put("latitude", Double.parseDouble(latString));
					jsonObject.put("longitude", Double.parseDouble(longString));
					SimpleDateFormat sdf = new SimpleDateFormat(onGoingTrip.getDate()+onGoingTrip.getTime()+":00.000");
					Date date = null;
					try {
						date = sdf.parse(onGoingTrip.getDate()+onGoingTrip.getTime()+":00.000");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long timeInMillisSinceEpoch = date.getTime();
					//long timeInMinutesSinceEpoch = timeInMillisSinceEpoch / (60 * 1000);
					jsonObject.put("datetime",timeInMillisSinceEpoch);
					/*
					JSONArray peopleJSONArray = new JSONArray();
					peopleJSONArray.put("John Doe");
					peopleJSONArray.put("Joe Smith");
					jsonObject.put("people", peopleJSONArray);*/

					OutputStreamWriter out = new   OutputStreamWriter(httpConn.getOutputStream());
					out.write(jsonObject.toString());
					out.close();
				}catch (JSONException je){

					je.printStackTrace();
				}
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

			long jsonData = 1111;
			try {
				jsonData = getDataFromJson(json);

			} catch (JSONException e) {
				e.printStackTrace();
			}



			//JSONObject myjson = new JSONObject(json);
			//= jsonData;



			return json;

		}


		public long getDataFromJson(String jString) throws JSONException {

			ArrayList<String> people = new ArrayList<String>();

			JSONObject myjson = new JSONObject(jString);


			long responseCode = myjson.getLong("response_code");

			//tripId = myjson.getInt("trip_id");


			//long[] response = new long[2];
			//response[0] = responseCode;
			//response[1] = tripId;

			return responseCode;

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

			try {
				//long[] responseValue = getDataFromJson(stream_url);
				if(getDataFromJson(stream_url) == 0) {
					//Toast.makeText(getBaseContext(), "Response value = " + getDataFromJson(stream_url), Toast.LENGTH_SHORT).show();
					Toast.makeText(getBaseContext(), "Successfully updated !!", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//response(stream_url);
		}
	}

	public void makeTripHttpPostRequest() {

		String urlstring = "http://cs9033-homework.appspot.com/";

		new CallAPI().execute(urlstring);

	}


}