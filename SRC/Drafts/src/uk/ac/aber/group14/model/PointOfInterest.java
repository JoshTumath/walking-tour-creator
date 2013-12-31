package uk.ac.aber.group14.model;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class PointOfInterest implements IPointOfInterest {
	private String name;
	private String shortDescription;
	private String longDescription;
	private LinkedList<Bitmap> pictures;
	private Location location;

	public PointOfInterest(Location location) {
		pictures = new LinkedList<Bitmap>();
		this.location = location;
	}
	
	private PointOfInterest(Parcel parcel) {
        name = parcel.readString();
        shortDescription = parcel.readString();
        longDescription = parcel.readString();
        pictures = new LinkedList<Bitmap>();
        parcel.readList(pictures, null);
        location = (Location)parcel.readParcelable(null);
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
	public void setlongDescription(String desc) {
		longDescription = desc;
	}

	@Override
	public void addPicture(Bitmap image) {
		pictures.add(image);
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
	public Bitmap[] getPictures() {
		return (Bitmap[]) pictures.toArray();
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

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeString(shortDescription);
		arg0.writeString(longDescription);
		arg0.writeList(pictures);
		arg0.writeParcelable(location, 0);
		
		return;
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
