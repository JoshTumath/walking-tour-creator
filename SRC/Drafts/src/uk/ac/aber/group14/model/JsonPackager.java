package uk.ac.aber.group14.model;

import org.json.JSONException;
import org.json.JSONObject;
import android.location.Location;

public class JsonPackager implements IJsonPackager {

	@Override
	public JSONObject JSONify(Walk w) {
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
			
			
			
			
			
			walk.put("data", walkData);
			walk.put("points", points);
		} catch (JSONException e) {
			// TODO XXX write proper error handling code
			e.printStackTrace();
		}
		
		
		
		
		return null;
	}

}
