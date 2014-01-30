package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.IUploadFinishNotify;
import uk.ac.aber.group14.controller.WalkController;
import uk.ac.aber.group14.controller.WalkUploader;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 
 *
 */
public class WalkCreatorActivity extends Activity implements LocationListener,
		IUploadFinishNotify, DialogInterface.OnDismissListener{
	private IWalkController walkController;
	private LocationManager locationManager ;
	private final int timerDelay = 5000; // Milliseconds
	private final int locationMinTime = 5000; // Milliseconds
	private final int locationMinDistance = 5; // Meters
	private Handler uiUpdateHandler;
	private boolean isRunning, isFinished=false, isUploading=false;
	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private WalkUploader walkUploader;

	/**
	 * onCreate is the core part of the Walk creator activity which houses a few key methods such as
	 *  the cancel walk,add location, record location, saving the route and change location
	 * tht5
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walk_creator);
		Intent myIntent = getIntent();
		
		progressDialog = new ProgressDialog(WalkCreatorActivity.this);
		alertDialog = new AlertDialog.Builder(WalkCreatorActivity.this).create();
		// simple saving of states in the walk Controller
		if(savedInstanceState!=null) {
			walkController = savedInstanceState.getParcelable("walkController");
			isRunning = savedInstanceState.getBoolean("isRunning");
			isFinished = savedInstanceState.getBoolean("isFinished");
			isUploading = savedInstanceState.getBoolean("isUploading");
			((TextView) this.findViewById(R.id.latitude)).setText(savedInstanceState.getString("latitude"));
			((TextView) this.findViewById(R.id.longitude)).setText(savedInstanceState.getString("longitude"));		
		}
		else
		{
			walkController = new WalkController(
					myIntent.getStringExtra("name"),
					myIntent.getStringExtra("shortDescription"),
					myIntent.getStringExtra("longDescription"));
		}
		
		
		WalkCreatorActivity lastActivity = (WalkCreatorActivity) getLastNonConfigurationInstance();
		if(lastActivity != null) { // this uploads the walk
			walkUploader = lastActivity.getWalkUploader();
			if(walkUploader != null) {
				walkUploader.setDialogsAndNotify(progressDialog, alertDialog, this);
			}
			if(walkUploader != null && isUploading) {
				progressDialog.show();
			}
		}
		
		
		
		alertDialog.setOnDismissListener(this);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationMinTime, locationMinDistance, this);


		uiUpdateHandler = new Handler();
		isRunning = true;
		
	}

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
		/**
		 * code that disables the physical back button on android phones
		 * tht5
		 */
		 public void onBackPressed() {
			 
		 }

	public void addLocation(View view) {//creates location activity
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(lastLocation != null)
		{
			Intent locationIntent = new Intent(this, LocationActivity.class);

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
		walkController.addLocation(location);
		((TextView) findViewById(R.id.latitude)).setText(Double.toString(location.getLatitude()));
		((TextView) findViewById(R.id.longitude)).setText(Double.toString(location.getLongitude()));
	}

	public void onLocationChanged(Location location) {
		// Called when a new location is found by the GPS location provider.
		Log.i("WTC", "New location found, adding...");
		recordNewLocation(location);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) { Log.i("WTC", "Provider " + provider + " enabled.");}

    public void onProviderDisabled(String provider) {Log.i("WTC", "Provider " + provider + "disabled.");}
    //saves the route 
    public void onSaveRoute(View view) {
    	if(walkController.canUpload())
    	{
    		isRunning = false;
    		locationManager.removeUpdates(this);
    		Log.i("WTC", "Attempting to upload walk...");
    		walkUploader = new WalkUploader();
    		walkUploader.setDialogsAndNotify(progressDialog, alertDialog, this);
    		walkUploader.execute(walkController);
    		isUploading = true;
    	}
    	else
    	{
    		Location currentLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
    		if(currentLocation != null) {
    			walkController.addLocation(currentLocation);
    		}
    		alertDialog.setTitle("Error");
    		alertDialog.setMessage("You need at least 1 Point of Interest and GPS Location to upload a walk!");
    		alertDialog.show();
    	}
    }
	@Override
	public void setFinished() {
		isFinished = true;
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		if(isFinished)
		{
			finish();
		} else {
			isUploading = false;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle out) {
		out.putParcelable("walkController", walkController);
		out.putBoolean("isRunning", isRunning);
		out.putBoolean("isFinished", isFinished);
		out.putBoolean("isUploading", isUploading);
		String latitude = (String) ((TextView) this.findViewById(R.id.latitude)).getText();
		String longitude = (String) ((TextView) this.findViewById(R.id.longitude)).getText();
		out.putString("latitude", latitude);
		out.putString("longitude", longitude);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}
	
	public WalkUploader getWalkUploader() {
		return walkUploader;
	}
}
