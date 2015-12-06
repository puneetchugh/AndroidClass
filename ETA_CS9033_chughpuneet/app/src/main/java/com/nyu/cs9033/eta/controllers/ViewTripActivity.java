package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewTripActivity extends Activity {

	ListView list;
	CustomAdapter adapter;
	public  ViewTripActivity CustomListView = null;
	public ArrayList<Trip> CustomListViewValuesArr = new ArrayList<Trip>();

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

		TablesDataSource tablesDataSource = new TablesDataSource(this);
		try {
			tablesDataSource.open();
		} catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
		final List<Trip> trips = tablesDataSource.getAllTrips();
		tablesDataSource.close();
		for (int i = 0; i < trips.size(); i++) {

			CustomListViewValuesArr.add( trips.get(i) );
		}

	}



	public void onItemClick(int mPosition) {
		Trip tempValues = (Trip) CustomListViewValuesArr.get(mPosition);


		ArrayList<String> people = tempValues.getNames();
		int loopCounter = 0;
		StringBuilder peopleStringBuilder = new StringBuilder();
		for (String individual : people) {
			peopleStringBuilder.append(individual);
			peopleStringBuilder.append("; ");
		}

		//peopleStringBuilder.setLength(peopleStringBuilder.length() - 1); // to remove ; after last name

		Intent forViewingAnItem = new Intent(this, ShowListItem.class);
		forViewingAnItem.putExtra("trip_name", tempValues.getTripName());
		forViewingAnItem.putExtra("people_on_the_trip", peopleStringBuilder.toString());
		forViewingAnItem.putExtra("trip_location", tempValues.getLocation());
		forViewingAnItem.putExtra("trip_date",tempValues.getDate());
		forViewingAnItem.putExtra("trip_time", tempValues.getTime());
		startActivity(forViewingAnItem);
		// SHOW ALERT
	}
}
