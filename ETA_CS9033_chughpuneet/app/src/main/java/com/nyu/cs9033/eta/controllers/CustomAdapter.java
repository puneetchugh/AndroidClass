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


    private Activity activity;
    private ArrayList<Trip> trips;
    private static LayoutInflater inflater=null;
    public Resources res;
    Trip tempValues=null;
    int i=0;


    public CustomAdapter(Activity activity, ArrayList<Trip> trips,Resources res) {


        this.activity = activity;
        this.trips = trips;
        this.res = res;


        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

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
        public TextView tripDateTime;
        public TextView tripPeople;

    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){


            vi = inflater.inflate(R.layout.showing_list, null);


            holder = new ViewHolder();
            holder.tripId = (TextView) vi.findViewById(R.id.trip_id);
            holder.tripLocation=(TextView)vi.findViewById(R.id.trip_location);
            holder.tripDateTime=(TextView)vi.findViewById(R.id.trip_date_time);
            holder.tripPeople=(TextView)vi.findViewById(R.id.trip_people);



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

            tempValues=null;
            tempValues = ( Trip ) trips.get( position );



            ArrayList<String> people = tempValues.getNames();
            int loopCounter = 0;
            StringBuilder peopleStringBuilder = new StringBuilder();
            for(String individual: people){
                peopleStringBuilder.append(individual);
                peopleStringBuilder.append("; ");
            }
            peopleStringBuilder.setLength(peopleStringBuilder.length() - 1); // to remove ; after last name
            String tripIdString = "Trip "+Long.toString(tempValues.getId()+1);
            holder.tripId.setText(tripIdString);
            holder.tripLocation.setText(tempValues.getLocation());
            holder.tripDateTime.setText(tempValues.getDate()+"  "+tempValues.getTime());
            holder.tripPeople.setText(peopleStringBuilder.toString());
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }


    private class OnItemClickListener  implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            ViewTripActivity viewTripActivity = (ViewTripActivity)activity;


            viewTripActivity.onItemClick(mPosition);
        }
    }
}
