package uk.ac.aber.group14.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class PointOfInterest implements IPointOfInterest {
	private String name;
	private String description;
	private Bitmap picture;
	private Location location;

	public PointOfInterest(Location location) {
		this.location = location;
	}
	
	private PointOfInterest(Parcel parcel) {
		name = parcel.readString();
		description = parcel.readString();
		picture = (Bitmap) parcel.readParcelable(null);
		location = (Location) parcel.readParcelable(null);
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDescription(String desc) {
		description = desc;
	}

	@Override
	public void addPicture(Bitmap image) {
		picture = image;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int describeContents() {
		// Apparently this method is required but rarely necessary to modify
		return 0;
	}

	public Bitmap getPicture() {
		return picture;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(name);
		parcel.writeString(description);
		parcel.writeParcelable(picture, 0);
		parcel.writeParcelable(location, 0);
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
