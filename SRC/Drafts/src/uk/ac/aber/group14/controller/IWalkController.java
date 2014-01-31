package uk.ac.aber.group14.controller;
import android.location.Location;
import android.os.Parcelable;
import uk.ac.aber.group14.model.*;

/**
 * This interface is used to control a class which implements
 * the IWalk interface. It can add Points of Interest and Locations,
 * as well as canceling the walk, returning whether the walk can be
 * uploaded, and compiling the walk into JSON.
 * 
 * @author Group14
 *
 */
public interface IWalkController extends Parcelable {
   
   /**
    * This method is used to add a class implementing IPointOfInterest
    * to the walk's list of IPointOfInterest
    * @param point The IPointOfInterest to add to the walk
    */
   public void addPOI(IPointOfInterest point);
   
   /**
    * This method is used to cancel the walk.
    */
   public void cancelWalk();
   
   /**
    * This method is used to add GPS Locations to the walk's list
    * of Locations
    * @param location The location to add to the walk's list of locations
    */
   public void addLocation(Location location);
   
   /**
    * This method is used to determine whether or not the walk meets
    * the requirements to upload to the server.
    * We require at least one IPointOfInterest and Location.
    * @return True or false, depending on whether the walk can be uploaded
    */
   public boolean canUpload();
   
   /**
    * This method is used to compile the walk into a String
    * representing it in JSON
    * @return A String representing the walk as a JSON object
    */
   public String compileWalk();
}
