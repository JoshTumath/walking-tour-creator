package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;
import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.JsonPackager;
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
import android.widget.TextView;

public class WalkCreatorActivity extends Activity implements LocationListener{
	IWalkController walkController;
	private LocationManager locationManager ;
	private final int timerDelay = 5000; // Milliseconds
	private final int locationMinTime = 5000; // Milliseconds
	private final int locationMinDistance = 5; // Meters
	private Handler uiUpdateHandler;
	private boolean isRunning;

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
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationMinTime, locationMinDistance, this);
		Log.i("WTC", "Requested updates");


		uiUpdateHandler = new Handler();
		isRunning = true;
		uiUpdateHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(isRunning) {
					Log.i("WTC", "Getting last known location");
					Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					Log.i("WTC", "Recording last known location");
					if(lastLocation != null) {
						Log.i("WTC", "Changing last known location to " + 
								lastLocation.getLongitude() + "," +
								lastLocation.getLatitude() + " at " + 
								lastLocation.getTime());
						TextView coordinateView = (TextView) findViewById(R.id.textView3);
						if(coordinateView != null) {
							String coordinateText = lastLocation.getLongitude() + "," +
									lastLocation.getLatitude();
							coordinateView.setText(coordinateText);
						}
					}
					uiUpdateHandler.postDelayed(this, timerDelay);
				}
			}
		}, timerDelay);

	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walk_creator, menu);
		return true;
	}

	public void cancelWalk(View view) {
		isRunning = false;
		finish();
	}
	//code the prevents back button
		 public void onBackPressed() {
		     // TODO Auto-generated method stub
			 //buildAlertMessageBackMessage();
		  //   super.onBackPressed();
		 }

	public void addLocation(View view) {//creates location activity
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(lastLocation != null)
		{
			Intent locationIntent = new Intent(this, LocationActivity.class);
			//Bundle b=new Bundle();
			//b.putParcelable("location", lastLocation);
			locationIntent.putExtra("location", lastLocation);
			Log.i("WTC", "testing addlocation, in wcA");//used for testing
			//passing the bundle into
			startActivityForResult(locationIntent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//recieves
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK) {
			IPointOfInterest point = data.getParcelableExtra("pointOfInterest");
			Log.i("WTC", "Adding new point of interest, " + point.getName());
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

    public void onProviderEnabled(String provider) { Log.i("WTC", "Provider " + provider + " enabled.");}

    public void onProviderDisabled(String provider) {Log.i("WTC", "Provider " + provider + "disabled.");}
    
    public void onSaveRoute(View view) {
    	if(walkController.canUpload())
    	{
    		walkController.uploadWalk();
    		finish();
    	}
    }
}
