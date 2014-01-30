package uk.ac.aber.group14.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is an implementation if IPointOfInterest.
 * It stores a String name, String description, Location
 * object, and a String for the URI of the picture on the
 * filesystem
 * @author Group14
 *
 */
public class PointOfInterest implements IPointOfInterest {
	private String name;
	private String description;
	private String picture;
	private Location location;

	/**
	 * This constructor takes a Location object to use
	 * for the PointOfInterest's location data
	 * @param location The Location to use for this PointOfInterest
	 */
	public PointOfInterest(Location location) {
		this.location = location;
		this.picture = null;
	}
	
	/**
	 * This constructor takes a Parcel and constructs a PointOfInterest
	 * out of it.
	 * This is used to re-create a PointOfInterest after it has been put
	 * in a Parcel
	 * @param parcel
	 */
	private PointOfInterest(Parcel parcel) {
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.picture = parcel.readString();
		this.location = (Location) parcel.readParcelable(null);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#addPicture(java.lang.String)
	 */
	@Override
	public void addPicture(String picture) {
		this.picture = picture;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getLocation()
	 */
	@Override
	public Location getLocation() {
		return this.location;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getLatitude()
	 */
	@Override
	public double getLatitude() {
		return this.location.getLatitude();
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getLongitude()
	 */
	@Override
	public double getLongitude() {
		return this.location.getLongitude();
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getTime()
	 */
	@Override
	public long getTime() {
		return this.location.getTime();
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// Apparently this method is required but rarely necessary to modify
		return 0;
	}

	/* (non-Javadoc)
	 * @see uk.ac.aber.group14.model.IPointOfInterest#getPicture()
	 */
	public String getPicture() {
		return this.picture;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.name);
		parcel.writeString(this.description);
		parcel.writeString(this.picture);
		parcel.writeParcelable(this.location, 0);
	}

	/**
	 * This is the Parcelable.Creator for the class. This is used
	 * to create a PointOfInterest from data in a Parcel
	 */
	public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {
		public PointOfInterest createFromParcel(Parcel parcel) {
			return new PointOfInterest(parcel);
		}

		public PointOfInterest[] newArray(int size) {
			return new PointOfInterest[size];
		}
	};	
}