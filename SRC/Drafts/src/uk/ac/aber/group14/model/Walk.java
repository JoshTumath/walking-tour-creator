package uk.ac.aber.group14.model;

import java.util.LinkedList;

import android.location.Location;

public class Walk implements IWalk {
	
	private String name;
	private String shortDescription;
	private String longDescription;
	private LinkedList<IPointOfInterest> points;
	private LinkedList<Location> locations;
	
	public Walk(String name, String shortDescription, String longDescription) {
		setName(name);
		setShortDescription(shortDescription);
		setLongDescription(longDescription);
		points = new LinkedList<IPointOfInterest>();
		locations = new LinkedList<Location>();
	}

	@Override
	public void addPointOfInterest(IPointOfInterest point) {
		points.add(point);
	}

	@Override
	public void addLocations(LinkedList<Location> locations) {
		this.locations.addAll(locations);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setShortDescription(String desc) {
		shortDescription = desc;
	}

	@Override
	public void setLongDescription(String desc) {
		longDescription = desc;
	}

	@Override
	public IPointOfInterest[] getPointsOfInterest() {
		return (IPointOfInterest[]) points.toArray();
	}

	@Override
	public Location[] getLocations() {
		return (Location[]) locations.toArray();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public String getLongDescription() {
		return longDescription;
	}
	
	@Override
	public void addLocation(Location location) {
		locations.add(location);
	}

}
