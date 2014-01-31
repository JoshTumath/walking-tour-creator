package uk.ac.aber.group14.model;

import java.util.LinkedList;

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
    * This method is used to add a picture to the IPointOfInterest.
    * This must be a valid path to a picture in the filesystem.
    * @param picture String representing the location of the picture in the filesystem
    */
   public void addPicture(String picture);

   /**
    * This method is used to add a collection of pictures to the
    * IPointOfInterest.
    * These must be valid paths to pictures in the filesystem.
    * @param pictures LinkedList of Strings representing the locations of the pictures in the filesystem
    */
   public void addPictures(LinkedList<String> pictures);
   
   /**
    * This method is used to get an array of the pictures in the IPointOfInterest
    * @return An array of strings representing the locations on the filesystem of the pictures
    */
   public String[] getPictures();
   
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
