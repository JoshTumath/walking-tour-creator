package uk.ac.aber.group14.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;


/**
 * This class is used to convert IWalk objects to JSON Strings
 * @author Group14
 *
 */
@SuppressWarnings("unused")
public class JsonPackager implements IJsonPackager {
   JSONObject walk;
   JSONObject walkData;
   JSONObject points;
   
   /**
    * This method converts the IWalk to a JSON String
    * @see uk.ac.aber.group14.model.IJsonPackager#JSONify(uk.ac.aber.group14.model.IWalk)
    */
   @Override
   public String JSONify(IWalk w) {
      this.walk = new JSONObject();
      this.points = new JSONObject();
      try {
         IPointOfInterest[] pointsOfInterest = w.getPointsOfInterest();
         Location[] locations = w.getLocations();
         
         int startTime = (int) (Math.min(pointsOfInterest[0].getLocation().getTime(), locations[0].getTime()) / 1000);
         int endTime = (int) (Math.max(pointsOfInterest[pointsOfInterest.length - 1].getLocation().getTime(), locations[locations.length - 1].getTime()) / 1000);
         int timeDelta = endTime - startTime;
         
         /* Create JSON data from walk (name, descriptions, timestamp).
            Create walkData JSON object and put this data into it */
         JSONifyWalkData(w, timeDelta);
         
         // Create JSON data from Locations and add it to points JSON object
         JSONifyLocations(locations);
         
         // Create JSON data from POIs and add it to the points JSON object
         JSONifyPointsOfInterest(pointsOfInterest);
         
         this.walk.put("data", walkData);
         this.walk.put("points", points);
         
         return walk.toString(2);
      } catch (JSONException e) {
         e.printStackTrace();
      }
      
      return null;
   }
   
   /**
    * Modifies the walkData JSONObject to contain the required fields
    * taken from the Walk that is passed in.
    * 
    * @param w - IWalk
    * @param timeDelta - long
    * @throws JSONException
    */
   private void JSONifyWalkData(IWalk w, long timeDelta) throws JSONException {
      this.walkData = new JSONObject();
      this.walkData.put("walkname", w.getName());
      this.walkData.put("shortdescription", w.getShortDescription());
      this.walkData.put("longdescription", w.getLongDescription());
      this.walkData.put("time", timeDelta);
   }
   
   /**
    * Creates JSONObjects for each normal location in the Location array
    * that is passed to the function.
    * 
    * As required by the JSON Specification :
    * Encodes the poiflag field as false
    * Encodes the poidata field as a null JSON value
    * Places the created JSONObject in the points JSONObject with a numeric key
    * 
    * @param loc - Locations[]
    * @throws JSONException
    */
   public void JSONifyLocations(Location[] loc) throws JSONException {
      JSONObject location;
      
      for (Location l: loc) {
         location = JSONifyGenericLocation(l.getLatitude(), l.getLongitude(), (int) l.getTime() / 1000);
         location.put("poiflag", false);
         location.put("poidata", JSONObject.NULL);
         
         this.points.put(String.valueOf(points.length() + 1), location);
      }
   }
   
   /**
    * Creates JSONObjects for each Point Of Interest in the PointOfInterest array
    * that is passed to the function.
    * 
    * As required by the JSON Specification :
    * Encodes the poiflag field as true
    * Encodes the poidata field as a JSONObject containing additional information.
    * Embeds a base64 of any photo attached to the point or a null JSON value
    * Places the created JSONObject in the points JSONObject with a numeric key
    * 
    * @param poi - PointOfInterest[]
    * @throws JSONException
    */
   private void JSONifyPointsOfInterest(IPointOfInterest[] poi) throws JSONException {
      JSONObject pointOfInterest;
      JSONObject poiData;
      
      for (IPointOfInterest p : poi) {
         pointOfInterest = JSONifyGenericLocation(p.getLatitude(), p.getLongitude(), (int) p.getTime() / 1000);
         pointOfInterest.put("poiflag", true);
         
         poiData = new JSONObject();
         poiData.put("locationname", p.getName());
         poiData.put("description", p.getDescription());
         
         String[] pictures = p.getPictures();
         if ( pictures != null && pictures.length != 0) {
            JSONArray photoArray = new JSONArray();
            for(String picture : pictures) {
               String encodedPhoto = EncodePhotoUpgrade(picture);
               
               if (encodedPhoto != null) {
                  photoArray.put(encodedPhoto);
               }
            }
            poiData.put("photos", photoArray);
         } else {
            poiData.put("photos", JSONObject.NULL);
         }
         
         pointOfInterest.put("poidata", poiData);
         this.points.put(String.valueOf(points.length() + 1), pointOfInterest);
      }
   }
   
   /**
    * Creates and returns the base location object used by both location
    * and pointOfInterest based on the supplied values.
    * 
    * @param lat - double
    * @param lng - double
    * @param timestamp - int
    * @return JSONObject
    * @throws JSONException
    */
   public JSONObject JSONifyGenericLocation(double lat, double lng, int timestamp) throws JSONException {
      JSONObject location = new JSONObject();
      
      location.put("latitude", lat);
      location.put("longitude", lng);
      location.put("timestamp", timestamp);
      
      return location;
   }
   
   /**
    * This method encodes a photo into a base64 String
    * @param photoURI The URI to the photo's file
    * @return A String containing the base64 String of the photo
    */
   private String EncodePhoto(String photoURI) {
      File imageFile = new File(photoURI);
      String returnValue = null;
      try {
         BufferedInputStream bufIStream = new BufferedInputStream(new FileInputStream(imageFile));
         long lFileLength = imageFile.length();
         int iFileLength = (int) lFileLength;
         
         if(lFileLength == iFileLength) {
            byte[] byteArray = new byte[iFileLength];
            int offset = 0;
            do {
               offset += bufIStream.read(byteArray, offset, iFileLength-offset);
            } while(offset < iFileLength);
            bufIStream.close();
            returnValue = Base64.encodeToString(byteArray, Base64.URL_SAFE);
         }
         else {
            bufIStream.close();
            throw new IOException("File larger than 2GB");
         }
         
      } catch (FileNotFoundException e) {
         returnValue = null;
      } catch (IOException e) {
         returnValue = null;
      }
      
      return returnValue;
   }
   
   
   /**
    * This method encodes a photo into a base64 String
    * @param photoURI The URI to the photo's file
    * @return A String containing the base64 String of the photo
    */
   public static String EncodePhotoUpgrade(String photoURI) {
      File imageFile = new File(photoURI);
      FileInputStream fis = null;
      
      try {
         fis = new FileInputStream(imageFile);
      } catch (FileNotFoundException e) {
         return null;
      }

      Bitmap bitmap = BitmapFactory.decodeStream(fis);
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, 50 , byteStream);
      byte[] byteArray = byteStream.toByteArray();
      
      return Base64.encodeToString(byteArray, Base64.URL_SAFE);
   }
   
   /**
    * This method is used to escape a String base64 String to be URL-safe
    * @param s The String to encode
    * @return The URL-safe base64 String
    */
   public String URLEncodeBase64(String s) {
      String encodedString;
      encodedString = s.replace("+", "%2B");
      encodedString = encodedString.replace("/", "%2F");
      encodedString = encodedString.replace("=", "%3D");
      
      return encodedString;
   }
   
   //---------------------------------
   
   /**
    * Modifies the walkData JSONObject to contain the required fields
    * taken from the Walk that is passed in.
    * 
    * @param w - IWalk
    * @param timeDelta - long
    * @throws JSONException
    */
   public JSONObject memSafeJSONifyWalkData(IWalk w, long timeDelta) throws JSONException {
      JSONObject returnJSONObject = new JSONObject();
      returnJSONObject.put("walkname", w.getName());
      returnJSONObject.put("shortdescription", w.getShortDescription());
      returnJSONObject.put("longdescription", w.getLongDescription());
      returnJSONObject.put("time", timeDelta);
      
      return returnJSONObject;
   }
   
   
   /**
    * Creates JSONObjects for each Point Of Interest in the PointOfInterest array
    * that is passed to the function.
    * 
    * As required by the JSON Specification :
    * Encodes the poiflag field as true
    * Encodes the poidata field as a JSONObject containing additional information.
    * Instead of embedding the base64 encoded images, this places a null byte in the JSON
    * Places the created JSONObject in the points JSONObject with a numeric key
    * 
    * @param poi - PointOfInterest[]
    * @throws JSONException
    */
   public void memSafeJSONifyPointsOfInterest(JSONObject jsonPoints, IPointOfInterest[] poi) throws JSONException {
      JSONObject pointOfInterest;
      JSONObject poiData;
      
      for (IPointOfInterest p : poi) {
         pointOfInterest = JSONifyGenericLocation(p.getLatitude(), p.getLongitude(), (int) p.getTime() / 1000);
         pointOfInterest.put("poiflag", true);
         
         poiData = new JSONObject();
         poiData.put("locationname", p.getName());
         poiData.put("description", p.getDescription());
         
         String[] pictures = p.getPictures();
         if ( pictures != null && pictures.length != 0) {
            JSONArray photoArray = new JSONArray();
            for(String picture : pictures) {
               photoArray.put("\0");
            }
            poiData.put("photos", photoArray);
         } else {
            poiData.put("photos", JSONObject.NULL);
         }
         
         pointOfInterest.put("poidata", poiData);
         jsonPoints.put(String.valueOf(points.length() + 1), pointOfInterest);
      }
   }
   
   /**
    * Creates JSONObjects for each normal location in the Location array
    * that is passed to the function.
    * 
    * As required by the JSON Specification :
    * Encodes the poiflag field as false
    * Encodes the poidata field as a null JSON value
    * Places the created JSONObject in the points JSONObject with a numeric key
    * 
    * @param loc - Locations[]
    * @throws JSONException
    */
   public void memSafeJSONifyLocations(JSONObject jsonPoints, Location[] loc) throws JSONException {
      JSONObject location;
      
      for (Location l: loc) {
         location = JSONifyGenericLocation(l.getLatitude(), l.getLongitude(), (int) l.getTime() / 1000);
         location.put("poiflag", false);
         location.put("poidata", JSONObject.NULL);
         
         jsonPoints.put(String.valueOf(points.length() + 1), location);
      }
   }
   
   /**
    * This method converts the IWalk to a JSON String
    * @see uk.ac.aber.group14.model.IJsonPackager#JSONify(uk.ac.aber.group14.model.IWalk)
    */
   public String memSafeJSONify(IWalk w) {
      this.walk = new JSONObject();
      this.points = new JSONObject();
      try {
         IPointOfInterest[] pointsOfInterest = w.getPointsOfInterest();
         Location[] locations = w.getLocations();
         
         int startTime = (int) (Math.min(pointsOfInterest[0].getLocation().getTime(), locations[0].getTime()) / 1000);
         int endTime = (int) (Math.max(pointsOfInterest[pointsOfInterest.length - 1].getLocation().getTime(), locations[locations.length - 1].getTime()) / 1000);
         int timeDelta = endTime - startTime;
         
         /* Create JSON data from walk (name, descriptions, timestamp).
            Create walkData JSON object and put this data into it */
         memSafeJSONifyWalkData(w, timeDelta);
         
         // Create JSON data from Locations and add it to points JSON object
         memSafeJSONifyLocations(points, locations);
         
         // Create JSON data from POIs and add it to the points JSON object
         memSafeJSONifyPointsOfInterest(points, pointsOfInterest);
         
         this.walk.put("data", walkData);
         this.walk.put("points", points);
         
         return walk.toString(2);
      } catch (JSONException e) {
         e.printStackTrace();
      }
      return null;
   }
}
