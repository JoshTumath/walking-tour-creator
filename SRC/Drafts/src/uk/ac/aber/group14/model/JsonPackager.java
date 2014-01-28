package uk.ac.aber.group14.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
<<<<<<< HEAD
import java.io.FileNotFoundException;
import android.content.ContentResolver;
=======
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

>>>>>>> 058981881eb0fef781e7fb1207d6e88b5a783732
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
					File imageFile = new File(pointsOfInterest[i].getPicture());
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
							poiData.put("photo", Base64.encodeToString(byteArray,Base64.DEFAULT));
						}
						else {
							throw new IOException("File larger than 2GB");
						}
					} catch (FileNotFoundException e) {
						poiData.put("photo", JSONObject.NULL);
					} catch (IOException e) {
						poiData.put("photo", JSONObject.NULL);
					}

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
