package uk.ac.aber.group14.controller;
import android.location.Location;
import uk.ac.aber.group14.model.*;

public interface IWalkController {
	
	public void addPOI(IPointOfInterest point);
	
	public void cancelWalk();
	
	public String compileWalk();
	
	public void addLocation(Location location);
	
	public boolean canUpload();
}
