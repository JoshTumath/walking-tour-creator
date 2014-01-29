package uk.ac.aber.group14.controller;
import android.location.Location;
import android.os.Parcelable;
import uk.ac.aber.group14.model.*;

public interface IWalkController extends Parcelable {
	
	public void addPOI(IPointOfInterest point);
	
	public void cancelWalk();
	
	public String compileWalk();
	
	public void addLocation(Location location);
	
	public boolean canUpload();
}
