package uk.ac.aber.group14.model;

import java.util.LinkedList;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Walk implements IWalk, Parcelable {
	
	private String name;
	private String shortDescription;
	private String longDescription;
	private LinkedList<IPointOfInterest> points;
	private LinkedList<Location> locations;
	
	public Walk(String name, String shortDescription, String longDescription) {
		setName(name);
		setShortDescription(shortDescription);
		setLongDescription(longDescription);
		points = new LinkedList<IPointOfInterest>();
		locations = new LinkedList<Location>();
	}

	public Walk(Parcel source) {
		name = source.readString();
		shortDescription = source.readString();
		longDescription = source.readString();
		points = new LinkedList<IPointOfInterest>();
		source.readList(points, null);
		locations = new LinkedList<Location>();
		source.readList(locations, null);
	}

	@Override
	public void addPointOfInterest(IPointOfInterest point) {
		points.add(point);
	}

	@Override
	public void addLocations(LinkedList<Location> locations) {
		this.locations.addAll(locations);
	}
	
	@Override
	public void addLocation(Location location) {
		locations.add(location);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setShortDescription(String desc) {
		shortDescription = desc;
	}

	@Override
	public void setLongDescription(String desc) {
		longDescription = desc;
	}

	@Override
	public PointOfInterest[] getPointsOfInterest() {
		return points.toArray(new PointOfInterest[points.size()]);
	}

	@Override
	public Location[] getLocations() {
		return locations.toArray(new Location[locations.size()]);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String getLongDescription() {
		return longDescription;
	}

	@Override
	public int getNumberLocations() {
		return locations.size();
	}

	@Override
	public int getNumberPOI() {
		return points.size();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(shortDescription);
		out.writeString(longDescription);
		out.writeList(points);
		out.writeList(locations);
	}
	
	public static final Parcelable.Creator<Walk> CREATOR = new Parcelable.Creator<Walk>() {

		@Override
		public Walk createFromParcel(Parcel source) {
			return new Walk(source);
		}

		@Override
		public Walk[] newArray(int size) {
			return new Walk[size];
		}
		
	}; 
	
}
