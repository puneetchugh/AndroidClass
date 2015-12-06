package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ListView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

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

public class ViewTripActivity extends Activity {

	private TablesDataSource tablesDataSource;
	private ListView list;
	private CustomAdapter adapter;
	public  ViewTripActivity CustomListView = null;
	public ArrayList<Trip> CustomListViewValuesArr = new ArrayList<Trip>();
	private Trip tempValues;
	private String latString;
	private String longString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_trip_activity);


		CustomListView = this;

		setListData();

		Resources res =getResources();
		list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )


		adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
		list.setAdapter( adapter );

	}


	public void setListData()
	{

		tablesDataSource = new TablesDataSource(this);
		try {
			tablesDataSource.open();
		} catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
		final List<Trip> trips = tablesDataSource.getAllTrips();
		//tablesDataSource.close();
		for (int i = 0; i < trips.size(); i++) {

			CustomListViewValuesArr.add( trips.get(i) );
		}

	}



	public void onItemClick(int mPosition) {
		tempValues = (Trip) CustomListViewValuesArr.get(mPosition);

		final Intent forViewingAnItem = new Intent(this, ShowListItem.class);
		ArrayList<String> people = tempValues.getNames();
		int loopCounter = 0;
		Toast.makeText(this, "Latitude = " + tempValues.getLoc_latitude()+"Longitude = "+tempValues.getLoc_longitude(), Toast.LENGTH_SHORT).show();
		final StringBuilder peopleStringBuilder = new StringBuilder();
		for (String individual : people) {
			peopleStringBuilder.append(individual);
			peopleStringBuilder.append("; ");
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Select option");

		// set dialog message
		alertDialogBuilder
				.setMessage("Click on the option to chose")
				.setCancelable(false)
				.setPositiveButton("View",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						peopleStringBuilder.setLength(peopleStringBuilder.length() - 1); // to remove ; after last name

						forViewingAnItem.putExtra("trip_name", tempValues.getTripName());
						forViewingAnItem.putExtra("people_on_the_trip", peopleStringBuilder.toString());
						forViewingAnItem.putExtra("trip_location", tempValues.getLocation());
						forViewingAnItem.putExtra("trip_date",tempValues.getDate());
						forViewingAnItem.putExtra("trip_time", tempValues.getTime());


						startActivity(forViewingAnItem);
						finish();
					}
				})
				.setNegativeButton("Update", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						updateLocation(tempValues.getId());

						Intent intent = getIntent();
						finish();
						startActivity(intent);
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
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
			SystemClock.sleep(1000);
			tablesDataSource.updateLocation(tripId, latString, longString);
		}

		catch (NullPointerException e){
			e.printStackTrace();
		}

	}

	public static long getDataFromJson(String jString) throws JSONException {

		ArrayList<String> people = new ArrayList<String>();

		JSONObject myjson = new JSONObject(jString);


		long responseCode = myjson.getLong("response_code");

		//tripId = myjson.getInt("trip_id");


		//long[] response = new long[2];
		//response[0] = responseCode;
		//response[1] = tripId;

		return responseCode;

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
					jsonObject.put("command", "UPDATE_LOCATION");
					//jsonObject.put("location", jsonArray );
					jsonObject.put("latitude", Double.parseDouble(latString));
					jsonObject.put("longitude", Double.parseDouble(longString));
					SimpleDateFormat sdf = new SimpleDateFormat(tempValues.getDate()+tempValues.getTime()+":00.000");
					Date date = null;
					try {
						date = sdf.parse(tempValues.getDate()+tempValues.getTime()+":00.000");
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
