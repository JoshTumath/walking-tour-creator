package uk.ac.aber.group14.controller;

import java.util.LinkedList;

import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.JsonPackager;
import uk.ac.aber.group14.model.Walk;

public class WalkControllerPrototype implements IWalkController {
	private IWalk walk;
	private LinkedList<Location> locations;
	
	public WalkControllerPrototype(String name, String sd, String ld) {
		this.walk = new Walk(name, sd, ld);
		locations = new LinkedList<Location>();
	}
	
	@Override
	public void addPOI(IPointOfInterest point) {
		walk.addPointOfInterest(point);
	}

	@Override
	public void cancelWalk() {
		locations.clear();
		locations = null;
		walk = null;
	}

	@Override
	public Location getCurrentLocation() {
		return locations.getLast();
	}

	@Override
	public void uploadWalk() {
		IJsonPackager jsonPackager = new JsonPackager();
		JSONObject walkObject = jsonPackager.JSONify(walk);
		Log.i("WTC", "\n\n=== JSON WALK===\n\n" + walkObject.toString() + "\n\n================");
		
	}

	public void addLocation(Location location) {
		walk.addLocation(location);
	}
}
