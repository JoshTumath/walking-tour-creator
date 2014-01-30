package uk.ac.aber.group14.controller;

import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.JsonPackager;
import uk.ac.aber.group14.model.Walk;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class WalkController implements IWalkController, Parcelable {
	private IWalk walk;
	
	public WalkController(String name, String sd, String ld) {
		this.walk = new Walk(name, sd, ld);
	}
	
	public WalkController(Parcel source) {
		this.walk = (Walk) source.readParcelable(Walk.class.getClassLoader());
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
	public String compileWalk() {
		IJsonPackager jsonPackager = new JsonPackager();
		return jsonPackager.JSONify(walk);
	}

	public void addLocation(Location location) {
		walk.addLocation(location);
	}

	@Override
	public boolean canUpload() {
		return (walk.getNumberLocations() > 0 && walk.getNumberPOI() > 0);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(walk, flags);
	}
	
	public static final Parcelable.Creator<WalkController> CREATOR = new Parcelable.Creator<WalkController>() {

		@Override
		public WalkController createFromParcel(Parcel source) {
			return new WalkController(source);
		}

		@Override
		public WalkController[] newArray(int size) {
			return new WalkController[size];
		}
		
	}; 
}
