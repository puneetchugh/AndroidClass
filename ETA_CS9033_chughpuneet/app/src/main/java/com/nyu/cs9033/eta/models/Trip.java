package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {
	
	// Member fields should exist here, what else do you need for a trip?
	// Please add additional fields

	private String location;
	private String time;
	private String date;
	/*
	private String firstPerson;
	private String secondPerson;
	private String thirdPerson;
	private String fourthPerson;
	private String fifthPerson;
	*/
	private String tripName;

	ArrayList<Person> manyPerson = new ArrayList<Person>();
	private int numberOfPerson;
	
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
		
		// TODO - fill in here
		//String[] data = new String[8];
		//p.readStringArray(data);

		this.location = p.readString();
		this.date = p.readString();
		this.time = p.readString();

		/*
		this.firstPerson = p.readString();
		this.secondPerson = p.readString();
		this.thirdPerson = p.readString();
		this.fourthPerson = p.readString();
		this.fifthPerson = p.readString();
		*/
		this.tripName = p.readString();

		this.numberOfPerson = Integer.parseInt(p.readString());
		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			this.manyPerson.add(new Person(p.readString()));
		}

	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 *
	 * @param
	 */
	public Trip(String location, String date, String time, String tripName, ArrayList<Person> manyPerson) {
		
		// TODO - fill in here, please note you must have more arguments here


		this.location = location;
		this.date = date;
		this.time = time;
		/*
		this.firstPerson = firstPerson;
		this.secondPerson = secondPerson;
		this.thirdPerson = thirdPerson;
		this.fourthPerson = fourthPerson;
		this.fifthPerson = fifthPerson;
		*/
		this.tripName = tripName;

		//this.numberOfPerson = numberOfPerson;

		/*for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			this.manyPerson[loopCounter] = new Person(manyPerson[loopCounter]);
			*/
		int loopCounter = 0;
		for(Person newPerson: manyPerson){
			this.manyPerson.add(new Person(newPerson.getName()));

		}

		this.numberOfPerson = manyPerson.size();


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
		
		// TODO - fill in here
//		dest.writeStringArray(new String[] {

		dest.writeString(this.location);
		dest.writeString(this.date);
		dest.writeString(this.time);

				/*
				dest.writeString(this.firstPerson);
				dest.writeString(this.secondPerson);
				dest.writeString(this.thirdPerson);
				dest.writeString(this.fourthPerson);
				dest.writeString(this.fifthPerson);
				*/
		dest.writeString(this.tripName);

		String totalNumber = Integer.toString(numberOfPerson);
		dest.writeString(totalNumber);
		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			dest.writeString(this.manyPerson.get(loopCounter).getName());
		}
//		});
	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */

	public String getLocation(){
		return location;
	}

	public String getTime(){
		return time;
	}

	public String getDate(){
		return date;
	}

	/*
	public String getFirstPerson(){
		return firstPerson;
	}

	public String getSecondPerson(){
		return secondPerson;
	}

	public String getThirdPerson(){
		return thirdPerson;
	}

	public String getFourthPerson(){
		return fourthPerson;
	}

	public String getFifthPerson(){
		return fifthPerson;
	}
	*/
	public String getTripName(){
		return tripName;
	}

	public ArrayList<String> getNames(){
		//int loopCounter = 0;
		ArrayList<String> names = new ArrayList<String>();

		for(int loopCounter = 0; loopCounter < numberOfPerson; loopCounter++){
			names.add(new String(manyPerson.get(loopCounter).getName()));
		}
		return names;
		/*
		for(Person person: manyPerson[loopCounter]){

		}*/
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
