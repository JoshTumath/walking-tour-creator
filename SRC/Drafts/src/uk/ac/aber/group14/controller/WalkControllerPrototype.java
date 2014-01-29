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
	public String compileWalk() {
		IJsonPackager jsonPackager = new JsonPackager();
		return jsonPackager.JSONify(walk);
	}

	/*@Override
	public boolean uploadWalk() {
		boolean uploadSuccess=true;
		IJsonPackager jsonPackager = new JsonPackager();
		Log.i("WTC", "Number of locations:	" + walk.getLocations().length + "\n" +
				"Number of points:	" + walk.getPointsOfInterest().length);
		String walkObject = jsonPackager.JSONify(walk);
		Log.i("WTC", "\n\n=== JSON WALK===\n\n" + walkObject + "\n\n================");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://jakemaguire.co.uk/projects/wtc/upload.php");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("posttourdata", walkObject));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			Log.i("WTC", "Error attempting to encode URL: " + e.getLocalizedMessage());
			uploadSuccess=false;
		}
		try {
			httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess=false;
		} catch (IOException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess=false;
		}
		return uploadSuccess;
	}*/

	public void addLocation(Location location) {
		walk.addLocation(location);
	}

	@Override
	public boolean canUpload() {
		return (walk.getNumberLocations() > 0 && walk.getNumberPOI() > 0);
	}
}
