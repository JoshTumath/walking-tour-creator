package uk.ac.aber.group14.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.JsonPackager;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.aber.group14.model.IWalk;
import uk.ac.aber.group14.model.PointOfInterest;
import uk.ac.aber.group14.model.Walk;

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
