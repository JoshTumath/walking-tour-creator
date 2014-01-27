package uk.ac.aber.group14.controller;

import java.util.LinkedList;

import android.location.Location;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.PointOfInterest;
import uk.ac.aber.group14.model.Walk;

public class WalkControllerPrototype implements IWalkController {
	private IWalk walk;
	private LinkedList<Location> locations;
	
	public WalkControllerPrototype(String name, String sd, String ld) {
		this.walk = new Walk(name, sd, ld);
		locations = new LinkedList<Location>();
	}
	
	@Override
	public void addPOI(PointOfInterest point) {
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
		// TODO Auto-generated method stub
		
	}

	public void addLocation(Location location) {
		walk.addLocation(location);
	}
}
