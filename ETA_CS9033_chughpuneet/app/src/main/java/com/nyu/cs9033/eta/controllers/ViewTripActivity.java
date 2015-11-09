package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

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

		/******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
		setListData();

		Resources res =getResources();
		list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

		/**************** Create Custom Adapter *********/
		adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
		list.setAdapter( adapter );

	}

	/****** Function to set data in ArrayList *************/
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


	/*****************  This function used by adapter ****************/
	public void onItemClick(int mPosition)
	{
		Trip tempValues = ( Trip) CustomListViewValuesArr.get(mPosition);


		// SHOW ALERT

		Toast.makeText(CustomListView,
				"" + tempValues.getTripName()
						+ " " + tempValues.getNumberOfPerson() +
						" " + tempValues.getLocation() +
						" " + tempValues.getTime(),
		Toast.LENGTH_LONG)
		.show();
	}
}
