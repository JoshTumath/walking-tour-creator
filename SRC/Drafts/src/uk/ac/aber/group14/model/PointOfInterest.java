package uk.ac.aber.group14.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class PointOfInterest implements IPointOfInterest {
	private String name;
	private String description;
	private String picture;
	private Location location;

	public PointOfInterest(Location location) {
		this.location = location;
		this.picture = null;
	}
	
	private PointOfInterest(Parcel parcel) {
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.picture = parcel.readString();
		this.location = (Location) parcel.readParcelable(null);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

	@Override
	public void addPicture(String image) {
		this.picture = image;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public int describeContents() {
		// Apparently this method is required but rarely necessary to modify
		return 0;
	}

	public String getPicture() {
		return this.picture;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(this.name);
		parcel.writeString(this.description);
		parcel.writeString(this.picture);
		parcel.writeParcelable(this.location, 0);
	}

	public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {
		public PointOfInterest createFromParcel(Parcel parcel) {
			return new PointOfInterest(parcel);
		}

		public PointOfInterest[] newArray(int size) {
			return new PointOfInterest[size];
		}
	};


}
