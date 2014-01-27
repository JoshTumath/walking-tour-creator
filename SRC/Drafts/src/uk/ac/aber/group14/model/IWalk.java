package uk.ac.aber.group14.model;

import android.location.Location;

public interface IWalk {

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
	
}
