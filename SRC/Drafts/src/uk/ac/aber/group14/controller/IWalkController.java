package uk.ac.aber.group14.controller;
import android.location.Location;
import uk.ac.aber.group14.model.*;

public interface IWalkController {
	
	public void addPOI(PointOfInterest point);
	
	public android.location.Location getCurrentLocation();
	
	public void cancelWalk();
	
	public void uploadWalk();
	
	public void addLocation(Location location);
}
