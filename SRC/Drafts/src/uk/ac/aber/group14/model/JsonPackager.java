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

public class JsonPackager implements IJsonPackager {
	JSONObject walk;
	JSONObject walkData;
	JSONObject points;

	@Override
	public String JSONify(IWalk w) {
		JSONObject walk = new JSONObject();
		
		try {
			PointOfInterest[] pointsOfInterest = w.getPointsOfInterest();
			Location[] locations = w.getLocations();
			
			//TODO This will break if there are no location or points of interest
			int startTime = (int) (Math.min(pointsOfInterest[0].getLocation().getTime(), locations[0].getTime()) / 1000);
			int endTime = (int) (Math.max(pointsOfInterest[pointsOfInterest.length - 1].getLocation().getTime(), locations[locations.length - 1].getTime()) / 1000);
			int timeDelta = endTime - startTime;
			
			JSONifyWalkData(w, timeDelta);
			JSONifyLocations(locations);
			JSONifyPointsOfInterest(pointsOfInterest);
			
			walk.put("data", walkData);
			walk.put("points", points);
			
			return walk.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void JSONifyWalkData(IWalk w, long timeDelta) throws JSONException {
		this.walkData = new JSONObject();
		this.walkData.put("walkname", w.getName());
		this.walkData.put("shortdescription", w.getShortDescription());
		this.walkData.put("longdescription", w.getLongDescription());
		this.walkData.put("time", timeDelta);
	}
	
	private void JSONifyLocations(Location[] loc) throws JSONException {
		JSONObject location;
		
		for (Location l: loc) {
			location = JSONifyGenericLocation(l.getLatitude(), l.getLongitude(), l.getTime());
			location.put("poiflag", false);
			location.put("poidata", JSONObject.NULL);
			
			points.put(String.valueOf(points.length() + 1), location);
		}
	}
	
	private void JSONifyPointsOfInterest(PointOfInterest[] poi) throws JSONException {
		JSONObject pointOfInterest;
		JSONObject poiData;
		
		for (PointOfInterest p : poi) {
			pointOfInterest = JSONifyGenericLocation(p.getLatitude(), p.getLongitude(), p.getTime());
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
			points.put(String.valueOf(points.length() + 1), pointOfInterest);
		}
	}
	
	private JSONObject JSONifyGenericLocation(double lat, double lng, long timestamp) throws JSONException {
		JSONObject location = new JSONObject();
		
		location.put("latitude", lat);
		location.put("longitude", lng);
		location.put("timestamp", timestamp);
		
		return location;
	}
	
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
	
	private String URLEncodeBase64(String s) {
		String encodedString;
		encodedString = s.replace("+", "%2B");
		encodedString = encodedString.replace("/", "%2F");
		encodedString = encodedString.replace("=", "%3D");
		
		return encodedString;
	}
	
}
