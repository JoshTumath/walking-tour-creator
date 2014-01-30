package uk.ac.aber.group14.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;


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
	
	/* (non-Javadoc)
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
			
			//TODO This will break if there are no location or points of interest
			int startTime = (int) (Math.min(pointsOfInterest[0].getLocation().getTime(), locations[0].getTime()) / 1000);
			int endTime = (int) (Math.max(pointsOfInterest[pointsOfInterest.length - 1].getLocation().getTime(), locations[locations.length - 1].getTime()) / 1000);
			int timeDelta = endTime - startTime;
			
			JSONifyWalkData(w, timeDelta);
			JSONifyLocations(locations);
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
	private void JSONifyLocations(Location[] loc) throws JSONException {
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
			
			Log.i("WTC", "Testing to see if POI contains a picture...");
			if (p.getPicture() != null) {
				String encodedPhoto = EncodePhoto(p.getPicture());
				
				if (encodedPhoto == null) {
					poiData.put("photo", JSONObject.NULL);
				} else {
					poiData.put("photo", encodedPhoto);
				}
			} else {
				poiData.put("photo", JSONObject.NULL);
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
	private JSONObject JSONifyGenericLocation(double lat, double lng, int timestamp) throws JSONException {
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
		Log.i("WTC", "Attempting to convert picture to Base64 string");
		File imageFile = new File(photoURI);
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
				return Base64.encodeToString(byteArray, Base64.URL_SAFE);
			}
			else {
				bufIStream.close();
				Log.i("WTC", "IOException");
				throw new IOException("File larger than 2GB");
			}
			
		} catch (FileNotFoundException e) {
			Log.i("WTC", e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			Log.i("WTC", e.getLocalizedMessage());
			return null;
		}
	}
	
	
	/**
	 * This method encodes a photo into a base64 String
	 * @param photoURI The URI to the photo's file
	 * @return A String containing the base64 String of the photo
	 */
	private String EncodePhotoUpgrade(String photoURI) {
		File imageFile = new File(photoURI);
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(imageFile);
		} catch (FileNotFoundException e) {
			Log.i("WTC", e.getLocalizedMessage());
			return null;
		}

		Bitmap bitmap = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , byteStream);
		byte[] byteArray = byteStream.toByteArray();
		
		return Base64.encodeToString(byteArray, Base64.URL_SAFE);
	}
	
	/**
	 * This method is used to escape a String base64 String to be URL-safe
	 * @param s The String to encode
	 * @return The URL-safe base64 String
	 */
	private String URLEncodeBase64(String s) {
		String encodedString;
		encodedString = s.replace("+", "%2B");
		encodedString = encodedString.replace("/", "%2F");
		encodedString = encodedString.replace("=", "%3D");
		
		return encodedString;
	}
	
}
