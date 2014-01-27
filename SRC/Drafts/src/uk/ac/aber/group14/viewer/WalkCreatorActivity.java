package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class WalkCreatorActivity extends Activity implements LocationListener{
	IWalkController walkController;
	private LocationManager locationManager ;
	private final int timerDelay = 5000;
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walk_creator);
		Intent myIntent = getIntent();
		walkController = new WalkControllerPrototype(
							myIntent.getStringExtra("name"),
							myIntent.getStringExtra("shortDescription"),
							myIntent.getStringExtra("longDescription"));
		Log.i("WTC", "Created walk using /" + myIntent.getStringExtra("name") +
				"/" + myIntent.getStringExtra("shortDescription") + 
				"/" + myIntent.getStringExtra("longDescription") + "/");


		
		
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Log.i("WTC", "Got the location manager");
		
		  Log.i("WTC", "Created the listener");
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		Log.i("WTC", "Requested updates");
		
		
		Handler uiUpdateHandler = new Handler();
		uiUpdateHandler.post(new Runnable() {
			@Override
			public void run() {
				Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Log.i("WTC", "Changing last know location to " + 
						lastLocation.getLongitude() + "," +
						lastLocation.getLatitude() + " at " + 
						lastLocation.getTime());
			}
		});
		
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walk_creator, menu);
		return true;
	}
	
	public void cancelWalk(View view) {
		finish();
	}
	
	public void addLocation(View view) {//creates location activity
		Bundle b=new Bundle();
		b.putString("mylocatKey", LOCATION_SERVICE);
		Log.i("WTC", "testing addlocation, in wcA");//used for testing
		//passing the bundle into
		Intent locationIntent = new Intent(this, LocationActivity.class);
		startActivityForResult(locationIntent, 1);
		
		locationIntent.putExtra("myLocationalKey",locationIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//recieves
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK) {
			IPointOfInterest point = (PointOfInterest)data.getSerializableExtra("pointOfInterest");
			walkController.addPOI(point);
		}
	}

	
	private void recordNewLocation(Location location) {
		Log.i("WTC", "Adding new location:" + location.getLatitude() + 
				"," + location.getLongitude() + " at " + location.getTime());
		walkController.addLocation(location);
	}

    	public void onLocationChanged(Location location) {
	      // Called when a new location is found by the GPS location provider.
	    	Log.i("WTC", "New location found, adding...");
	      recordNewLocation(location);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}


	    public void onProviderDisabled(String provider) {}
}
