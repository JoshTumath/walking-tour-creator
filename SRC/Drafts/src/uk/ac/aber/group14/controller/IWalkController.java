package uk.ac.aber.group14.controller;
import uk.ac.aber.group14.model.*;

public interface IWalkController {
	
	public void addPOI(IPointOfInterest point);
	
	public android.location.Location getCurrentLocation();
	
	public void cancelWalk();
	
	public void uploadWalk();
}
