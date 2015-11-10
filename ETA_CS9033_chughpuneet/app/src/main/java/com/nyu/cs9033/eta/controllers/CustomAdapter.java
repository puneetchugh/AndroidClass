package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

import java.util.ArrayList;

/**
 * Created by puneet on 11/8/15.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<Trip> trips;
    private static LayoutInflater inflater=null;
    public Resources res;
    Trip tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity activity, ArrayList<Trip> trips,Resources res) {

        /********** Take passed values **********/
        this.activity = activity;
        this.trips = trips;
        this.res = res;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(trips.size()<=0)
            return 1;
        return trips.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView tripId;
        public TextView tripLocation;
        //public TextView peopleGoingOnTrip;
        //public TextView tripLocation;
        //public TextView tripTime;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.showing_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.tripId = (TextView) vi.findViewById(R.id.trip_id);
            //holder.tripName = (TextView) vi.findViewById(R.id.trip_name_id);
            //holder.peopleGoingOnTrip=(TextView)vi.findViewById(R.id.peoples_name);
            holder.tripLocation=(TextView)vi.findViewById(R.id.trip_location);
            //holder.tripTime = (TextView)vi.findViewById(R.id.view_time_id);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(trips.size()<=0)
        {
            holder.tripId.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( Trip ) trips.get( position );

            /************  Set Model values in Holder elements ***********/

            ArrayList<String> people = tempValues.getNames();
            int loopCounter = 0;
            StringBuilder peopleStringBuilder = new StringBuilder();
            for(String individual: people){
                peopleStringBuilder.append(individual);
                peopleStringBuilder.append("; ");
            }
            peopleStringBuilder.setLength(peopleStringBuilder.length() - 1); // to remove ; after last name
            String tripIdString = "Trip "+Integer.toString(tempValues.getId()+1);
            holder.tripId.setText(tripIdString);
            //holder.tripName.setText( tempValues.getTripName() );
            //holder.peopleGoingOnTrip.setText(peopleStringBuilder.toString());
            holder.tripLocation.setText(tempValues.getLocation());
            //holder.tripTime.setText(tempValues.getTime());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            ViewTripActivity viewTripActivity = (ViewTripActivity)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            viewTripActivity.onItemClick(mPosition);
        }
    }
}
