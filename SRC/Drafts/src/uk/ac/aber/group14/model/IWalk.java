package uk.ac.aber.group14.model;

import android.location.Location;
import android.os.Parcelable;

/**
 * This interface is used to represent a walk
 * This stores a collection of Location objects,
 * a collection of IPointOfInterest objects,
 * a String name, String short description, and String
 * long description.
 * @author Group14
 *
 */
public interface IWalk extends Parcelable{

   /**
    * This method is used to add a single IPointOfInterest to the IWalk
    * @param point The point to add to the IWalk
    */
   public void addPointOfInterest(IPointOfInterest point);
   
   /**
    * This method is used to add a LinkedList of Location objects to the IWalk
    * @param locations The Locations to add to the IWalk
    */
   public void addLocations(java.util.LinkedList<android.location.Location> locations);
   
   /**
    * This method is used to set the name of the IWalk
    * @param name String representing the name of the walk
    */
   public void setName(String name);
   
   /**
    * This method is used to set the short description of the IWalk
    * @param desc String representing the short description of the IWalk
    */
   public void setShortDescription(String desc);
   
   /**
    * This method is used to set the long description of the IWalk
    * @param desc String representing the long description of the IWalk
    */
   public void setLongDescription(String desc);
   
   /**
    * This method is used to return the points of interest
    * as an array of PointOfInterest
    * @return An array of type PointOfInterest representing all the points of interest in the IWalk
    */
   public IPointOfInterest[] getPointsOfInterest();
   
   /**
    * This method is used to return the GPS locations as an array of
    * type Location
    * @return An array of type Location representing all of the Locations in the IWalk
    */
   public android.location.Location[] getLocations();
   
   /**
    * This method is used to get the name of the IWalk
    * @return A String representing the name of the IWalk
    */
   public String getName();
   
   /**
    * This method is used to get the short description of the IWalk
    * @return A String representing the short description of the IWalk
    */
   public String getShortDescription();
   
   /**
    * This method is used to get the long description of the IWalk
    * @return A String representing the long description of the IWalk
    */
   public String getLongDescription();
   
   /**
    * This method is used to add a single Location to the IWalk
    * @param location The Location to add to the walk
    */
   public void addLocation(Location location);
   
   /**
    * This method is used to get the number of Location objects
    * in the IWalk
    * @return An int representing the number of Location objects in the IWalk
    */
   public int getNumberLocations();
   
   /**
    * This method is used to get the number of IPointOfInterest objects
    * in the IWalk
    * @return An int representing the number of IPointOfInterest objects in the IWalk
    */
   public int getNumberPOI();
}
