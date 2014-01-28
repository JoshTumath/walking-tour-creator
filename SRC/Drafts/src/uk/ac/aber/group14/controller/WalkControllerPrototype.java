package uk.ac.aber.group14.controller;

import java.util.LinkedList;

import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.JsonPackager;
import android.location.Location;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.PointOfInterest;
import uk.ac.aber.group14.model.Walk;

public class WalkControllerPrototype implements IWalkController {
	private IWalk walk;
	
	public WalkControllerPrototype(String name, String sd, String ld) {
		this.walk = new Walk(name, sd, ld);
	}
	
	@Override
	public void addPOI(IPointOfInterest point) {
		walk.addPointOfInterest(point);
	}

	@Override
	public void cancelWalk() {
		walk = null;
	}

	@Override
	public void uploadWalk() {
		IJsonPackager jsonPackager = new JsonPackager();
		Log.i("WTC", "Number of locations:	" + walk.getLocations().length + "\n" +
				"Number of points:	" + walk.getPointsOfInterest().length);
		String walkObject = jsonPackager.JSONify(walk);
		Log.i("WTC", "\n\n=== JSON WALK===\n\n" + walkObject + "\n\n================");
		
	}

	public void addLocation(Location location) {
		walk.addLocation(location);
	}

	@Override
	public boolean canUpload() {
		return (walk.getNumberLocations() > 0 && walk.getNumberPOI() > 0);
	}
}
