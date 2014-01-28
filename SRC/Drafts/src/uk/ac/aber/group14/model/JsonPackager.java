package uk.ac.aber.group14.model;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import android.content.ContentResolver;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

public class JsonPackager implements IJsonPackager {

	@Override
	public String JSONify(IWalk w) {
		JSONObject walk = new JSONObject();
		JSONObject walkData = new JSONObject();
		JSONObject points = new JSONObject();
		
		try {
			PointOfInterest[] pointsOfInterest = w.getPointsOfInterest();
			Location[] locations = w.getLocations();
			
			int startTime = (int) (Math.min(pointsOfInterest[0].getLocation().getTime(), locations[0].getTime()) / 1000);
			int endTime = (int) (Math.max(pointsOfInterest[pointsOfInterest.length - 1].getLocation().getTime(), locations[locations.length - 1].getTime()) / 1000);
			int timeDelta = endTime - startTime;
			
			walkData.put("time", timeDelta);
			walkData.put("walkname", w.getName());
			walkData.put("shortdescription", w.getShortDescription());
			walkData.put("longdescription", w.getLongDescription());
			walkData.put("time", timeDelta);
			
			for (int i = 0; i < locations.length; i++) {
				JSONObject loc = new JSONObject();
				
				loc.put("latitude", locations[i].getLatitude());
				loc.put("longitude", locations[i].getLongitude());
				loc.put("time", ((int) locations[i].getTime() / 1000));
				loc.put("poiflag", false);
				loc.put("poidata", JSONObject.NULL);
				
				points.put(String.valueOf(i), loc);
			}
			
			for (int i = 0; i < pointsOfInterest.length; i++) {
				JSONObject loc = new JSONObject();
				JSONObject poiData = new JSONObject();
				
				loc.put("latitude", pointsOfInterest[i].getLocation().getLatitude());
				loc.put("longitude", pointsOfInterest[i].getLocation().getLongitude());
				loc.put("time", ((int) pointsOfInterest[i].getLocation().getTime() / 1000));
				loc.put("poiflag", true);
				
				poiData.put("locationname", pointsOfInterest[i].getName());
				poiData.put("description", pointsOfInterest[i].getDescription());
				
				if (pointsOfInterest[i].getPicture() != null) {
					Bitmap bitmap;
					// XXX Load Bitmap from URI, byte conversion and Base64 encoding is done.
					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					poiData.put("photo", Base64.encodeToString(byteArray, Base64.DEFAULT));
				} else {
					poiData.put("photo", JSONObject.NULL);
				}
				
				loc.put("poidata", poiData);
				points.put(String.valueOf(i + locations.length), loc);
			}
			
			
			walk.put("data", walkData);
			walk.put("points", points);
			
			
			return walk.toString(4);
		} catch (JSONException e) {
			// TODO XXX write proper error handling code
			e.printStackTrace();
		}
		
		return null;
	}
	
}
