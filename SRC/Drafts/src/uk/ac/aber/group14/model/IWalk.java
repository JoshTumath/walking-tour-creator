package uk.ac.aber.group14.model;

import android.location.Location;
import android.os.Parcelable;

public interface IWalk extends Parcelable{

	public void addPointOfInterest(IPointOfInterest point);
	
	public void addLocations(java.util.LinkedList<android.location.Location> location);
	
	public void setName(String name);
	
	public void setShortDescription(String desc);
	
	public void setLongDescription(String desc);
	
	public PointOfInterest[] getPointsOfInterest();
	
	public android.location.Location[] getLocations();
	
	public String getName();
	
	public String getShortDescription();
	
	public String getLongDescription();
	
	public void addLocation(Location location);
	
	public int getNumberLocations();
	
	public int getNumberPOI();
}
