package uk.ac.aber.group14.controller;

import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.JsonPackager;
import uk.ac.aber.group14.model.Walk;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is used to control a class implementing the IWalk
 * interface.
 * @see uk.ac.aber.group14.controller.IWalkController
 * @author Group14
 *
 */
public class WalkController implements IWalkController, Parcelable {
   private IWalk walk;
   
   /**
    * This is the constructor for the controller. It takes a name,
    * short description, and long description and initializes a walk
    * with these values.
    * 
    * @param name The name of the walk
    * @param sd The short description of the walk
    * @param ld The long description of the walk
    */
   public WalkController(String name, String sd, String ld) {
      this.walk = new Walk(name, sd, ld);
   }
   
   /**
    * This constructor takes a Parcel and reconstructs the walkController
    * by taking a Walk object from it.
    * @param source The Parcel containing the Walk object
    */
   public WalkController(Parcel source) {
      this.walk = (Walk) source.readParcelable(Walk.class.getClassLoader());
   }

   /**
    * This adds an IPointOfInterest to the walk.
    * @see uk.ac.aber.group14.controller.IWalkController#addPOI(uk.ac.aber.group14.model.IPointOfInterest)
    */
   @Override
   public void addPOI(IPointOfInterest point) {
      walk.addPointOfInterest(point);
   }

   /**
    * This adds a GPS Location to the walk
    * @see uk.ac.aber.group14.controller.IWalkController#addLocation(android.location.Location)
    */
   public void addLocation(Location location) {
      walk.addLocation(location);
   }
   
   /**
    * This cancels the walk
    * @see uk.ac.aber.group14.controller.IWalkController#cancelWalk()
    */
   @Override
   public void cancelWalk() {
      walk = null;
   }

   /**
    * This is used to determine if the walk has the necessary assets
    * to upload it to the server. This returns true if the walk
    * has at least one point of interest and at least one location.
    * @see uk.ac.aber.group14.controller.IWalkController#canUpload()
    */
   @Override
   public boolean canUpload() {
      return (walk.getNumberLocations() > 0 && walk.getNumberPOI() > 0);
   }
   
   /**
    * This compiles the walk into JSON using the IJsonPackager interface
    * @see uk.ac.aber.group14.controller.IWalkController#compileWalk()
    */
   @Override
   public String compileWalk() {
      IJsonPackager jsonPackager = new JsonPackager();
      return jsonPackager.JSONify(walk);
   }

   /**
    * @see android.os.Parcelable#describeContents()
    */
   @Override
   public int describeContents() {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * This is used to store the walk variable to a Parcel
    * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
    */
   @Override
   public void writeToParcel(Parcel out, int flags) {
      out.writeParcelable(walk, flags);
   }
   
   /**
    * This is used to make a Parcelable creator for our class.
    * It allows the class to be Parcelable, placed in a bundle,
    * and loaded out of a bundle.
    */
   public static final Parcelable.Creator<WalkController> CREATOR = new Parcelable.Creator<WalkController>() {

      @Override
      public WalkController createFromParcel(Parcel source) {
         return new WalkController(source);
      }

      @Override
      public WalkController[] newArray(int size) {
         return new WalkController[size];
      }
      
   }; 
}
