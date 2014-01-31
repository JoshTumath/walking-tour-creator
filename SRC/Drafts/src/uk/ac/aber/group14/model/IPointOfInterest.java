/*
 * @(#) IPointOfInterest.java 1.0 2014-01-31
 *
 * Copyright (c) 2014 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.group14.model;

import android.os.Parcelable;

/**
 * This interface is used represent a point of interest.
 * This will store a String name, String description,
 * Location object, and optional String representing
 * a picture associated with the point
 * @author Group14
 *
 */
public interface IPointOfInterest extends Parcelable {
   
   /**
    * This method is used to set the IPointOfInterest's name
    * @param name String representing the new name of the IPointOfInterest
    */
   public void setName(String name);
   
   /**
    * This method is used to get the IPointOfInterest's name
    * @return String representing the name of the IPointOfInterest
    */
   public String getName();
   
   /**
    * This method is used to set the IPointOfInterest's description
    * @param desc String representing the new description of the IPointOfInterest
    */
   void setDescription(String desc);
   
   /**
    * This method is used to get the IPointOfInterest's description
    * @return String representing the IPointOfInterest's description
    */
   public String getDescription();
   
   /**
    * This method is used to set the picture of the IPointOfInterest.
    * This must be a valid path to a picture in the filesystem.
    * @param picture String representing the location of the picture in the filesystem
    */
   public void addPicture(String picture);
   
   /**
    * This method is used to get the picture of the IPointOfInterest
    * @return A string representing the location on the filesystem of the picture
    */
   public String getPicture();
   
   /**
    * This method is used to get the IPointOfInterest's latitude
    * @return A double representing the IPointOfInterest's latitude
    */
   public double getLatitude();
   
   /**
    * This method is used to get the IPointOfInterest's longitude
    * @return A double representing the IPointOfInterest's longitude
    */
   public double getLongitude();
   
   /**
    * This method is used to get the IPointOfInterest's timestamp.
    * @return long timestamp in the unix epoch format.
    */
   public long getTime();
   
   /**
    * This method is used to get the IPointOfInterest's Location object
    * @return A Location object, representing the GPS location of the IPointOfInterest
    */
   public android.location.Location getLocation();
   
}
