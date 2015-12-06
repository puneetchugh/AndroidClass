package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {
	

	private long id;
	private String location;
	private String time;
	private String date;
	private String tripName;
	ArrayList<Person> manyPerson = new ArrayList<Person>();
	private int numberOfPerson;
	private String loc_latitude;
	private String loc_longitude;

	/**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	
	/**
	 * Create a Trip model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) {
		

		this.id = p.readLong();
		this.location = p.readString();
		this.date = p.readString();
		this.time = p.readString();
		this.tripName = p.readString();

		this.numberOfPerson = Integer.parseInt(p.readString());
		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			this.manyPerson.add(new Person(id, p.readString()));
		}
		this.loc_latitude = p.readString();
		this.loc_longitude = p.readString();

	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 *
	 * @param
	 */
	public Trip(long id, String location, String date, String time, String tripName, ArrayList<Person> manyPerson, String loc_latitude, String loc_longitude) {
		
		this.id = id;
		this.location = location;
		this.date = date;
		this.time = time;
		this.tripName = tripName;

		int loopCounter = 0;
		for(Person newPerson: manyPerson){
			this.manyPerson.add(new Person(id, newPerson.getName()));

		}

		this.numberOfPerson = manyPerson.size();
		this.loc_latitude = loc_latitude;
		this.loc_longitude = loc_longitude;


	}

	/**
	 * Serialize Trip object by using writeToParcel. 
	 * This function is automatically called by the
	 * system when the object is serialized.
	 * 
	 * @param dest Parcel object that gets written on 
	 * serialization. Use functions to write out the
	 * object stored via your member variables. 
	 * 
	 * @param flags Additional flags about how the object 
	 * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
	 * In our case, you should be just passing 0.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(this.id);
		dest.writeString(this.location);
		dest.writeString(this.date);
		dest.writeString(this.time);

		dest.writeString(this.tripName);

		String totalNumber = Integer.toString(numberOfPerson);
		dest.writeString(totalNumber);
		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			dest.writeString(this.manyPerson.get(loopCounter).getName());

		}
		dest.writeString(this.loc_latitude);
		dest.writeString(this.loc_longitude);

	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */

	public long getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getTime(){
		return time;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getDate(){
		return date;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getTripName(){
		return tripName;
	}

	public String getLoc_latitude(){

		return loc_latitude;
	}

	public String getLoc_longitude(){

		return loc_latitude;
	}


	public ArrayList<String> getNames(){

		ArrayList<String> names = new ArrayList<String>();

		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			names.add(new String(manyPerson.get(loopCounter).getName()));
		}
		return names;

	}

	public ArrayList<Person> getManyPerson(){
		return manyPerson;
	}
	public int getNumberOfPerson(){
		return numberOfPerson;
	}
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}


}
