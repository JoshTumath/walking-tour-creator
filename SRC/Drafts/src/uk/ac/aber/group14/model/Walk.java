package uk.ac.aber.group14.model;

import java.util.LinkedList;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is the implementation of IWalk used in the app.
 * This represents a walk using a String name, String
 * short description, String long description,
 * LinkedList of IPointOfInterest objects, and
 * a LinkedList of Location objects to store
 * the relevant data.
 * @author Group14
 *
 */
public class Walk implements IWalk, Parcelable {
	
	private String name;
	private String shortDescription;
	private String longDescription;
	private LinkedList<IPointOfInterest> points;
	private LinkedList<Location> locations;
	
	/**
	 * This constructor is used to initialize a walk with
	 * the given name, short description, and long description
	 * @param name
	 * @param shortDescription
	 * @param longDescription
	 */
	public Walk(String name, String shortDescription, String longDescription) {
		setName(name);
		setShortDescription(shortDescription);
		setLongDescription(longDescription);
		points = new LinkedList<IPointOfInterest>();
		locations = new LinkedList<Location>();
	}

	/**
	 * This constructor is used to create a Walk object
	 * from a Parcel
	 * @param source
	 */
	public Walk(Parcel source) {
		name = source.readString();
		shortDescription = source.readString();
		longDescription = source.readString();
		points = new LinkedList<IPointOfInterest>();
		source.readList(points, null);
		locations = new LinkedList<Location>();
		source.readList(locations, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#addPointOfInterest(uk.ac.aber.group14.model.IPointOfInterest)
	 */
	@Override
	public void addPointOfInterest(IPointOfInterest point) {
		points.add(point);
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#addLocations(java.util.LinkedList)
	 */
	@Override
	public void addLocations(LinkedList<Location> locations) {
		this.locations.addAll(locations);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#addLocation(android.location.Location)
	 */
	@Override
	public void addLocation(Location location) {
		locations.add(location);
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#setShortDescription(java.lang.String)
	 */
	@Override
	public void setShortDescription(String desc) {
		shortDescription = desc;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#setLongDescription(java.lang.String)
	 */
	@Override
	public void setLongDescription(String desc) {
		longDescription = desc;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getPointsOfInterest()
	 */
	@Override
	public IPointOfInterest[] getPointsOfInterest() {
		return points.toArray(new IPointOfInterest[points.size()]);
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getLocations()
	 */
	@Override
	public Location[] getLocations() {
		return locations.toArray(new Location[locations.size()]);
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return longDescription;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getNumberLocations()
	 */
	@Override
	public int getNumberLocations() {
		return locations.size();
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IWalk#getNumberPOI()
	 */
	@Override
	public int getNumberPOI() {
		return points.size();
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(shortDescription);
		out.writeString(longDescription);
		out.writeList(points);
		out.writeList(locations);
	}
	
	/**
	 * This creates the Parcelable.Creator used to create our walk from
	 * a Parcel
	 */
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