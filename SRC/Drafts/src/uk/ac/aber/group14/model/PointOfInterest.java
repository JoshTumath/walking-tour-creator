package uk.ac.aber.group14.model;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.location.Location;

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

}
