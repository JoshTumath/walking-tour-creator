package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.IUploadFinishNotify;
import uk.ac.aber.group14.controller.WalkController;
import uk.ac.aber.group14.controller.WalkUploader;
import uk.ac.aber.group14.model.IPointOfInterest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * This activity is used to actually record the walks.
 * It dislays the user's latitude and longitude, and allows the
 * user cancel or upload the walk, as well as adding a point of
 * interest. If the user does not have any GPS coordinates or points
 * of interest then they cannot upload, and a dialog will tell them this.
 * When the user adds a location the LocationActivity is launched.
 * When the user uploads the walk, the WalkUploader AsyncTask is executed
 * and a progress dialog shows on their screen until it is complete.
 * @author Group14
 *
 */
public class WalkCreatorActivity extends Activity
implements LocationListener, IUploadFinishNotify, DialogInterface.OnDismissListener {
	private IWalkController walkController;
	private LocationManager locationManager ;
	private final int locationMinTime = 0; // Milliseconds
	private final int locationMinDistance = 15; // Meters
	private final float gpsMinAccuracy = 8; // Meters
	private boolean isRunning, isFinished=false, isUploading=false;
	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private WalkUploader walkUploader;

	
	/* (non-Javadoc)
	 * This method is called when the activity is created.
	 * Here we initialize variables and check to see if there
	 * was a saved instance state. If one is found then we load
	 * our variables from it. This allows the app to keep the
	 * last latitude/longitude points displayed on-screen.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
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
			if(walkUploader != null && isUploading && ! isFinished) {
				progressDialog.show();
			} else {
				if(isFinished) {
					alertDialog.show();
				}
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


		new Handler();
		isRunning = true;
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walk_creator, menu);
		return true;
	}

	/**
	 * This method is called by the "cancel walk" button.
	 * This calls "finish()" and the activity exits
	 * @param view
	 */
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

	/**
	 * This method is called by the "add location" button.
	 * This launches the LocationActivity activity, giving it
	 * the last recorded GPS location.
	 * @param view
	 */
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

	/* (non-Javadoc)
	 * This method is used to wait for the LocationActivity's results.
	 * If the result code is RESULT_OK then we take the PointOfInterest from
	 * it and add it to the walk via the WalkController
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//recieves
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("WTC", "Activity result received[" + requestCode + "] " + 
		data.getAction() + ", " + data.getDataString());
		if(resultCode == Activity.RESULT_OK) {
			IPointOfInterest point = data.getParcelableExtra("pointOfInterest");
			Log.i("WTC", "Adding new point of interest, " + point.getName());
			walkController.addPOI(point);
		}
	}


	/**
	 * This method is called when new GPS data is received and needs to
	 * be added to the walk.
	 * This adds the location to the walk via the WalkController, and
	 * updates the latitude and longitude values on-screen
	 * 
	 * @param location A GPS Location to add to the walk
	 */
	private void recordNewLocation(Location location) {
		walkController.addLocation(location);
		((TextView) findViewById(R.id.latitude)).setText(Double.toString(location.getLatitude()));
		((TextView) findViewById(R.id.longitude)).setText(Double.toString(location.getLongitude()));
	}

	/* (non-Javadoc)
	 * This is called by the LocationManager when a new GPS Location is
	 * provided.
	 * If the accuracy of the location is less than that specified
	 * in gpsMinAccuracy then we discard it.
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location) {
		if(location.getAccuracy() <= gpsMinAccuracy) {
			recordNewLocation(location);
		}
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {}

    /* (non-Javadoc)
     * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
     */
    public void onProviderEnabled(String provider) { Log.i("WTC", "Provider " + provider + " enabled.");}

    /* (non-Javadoc)
     * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
     */
    public void onProviderDisabled(String provider) {Log.i("WTC", "Provider " + provider + "disabled.");}

    
    /**
     * This method is called by the "Upload Walk" button.
     * It determines if the walk can be uploaded (at least 1 POI,
     * 1 GPS Location) and then creates a new WalkUploader AsyncTask.
     * This is then provided with our alertDialog and progressDailog
     * and executed.
     * If the user cannot upload a walk then they are told so with an
     * alert dialog.
     * 
     * @param view
     */
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
    		Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		if(currentLocation != null) {
    			walkController.addLocation(currentLocation);
    		}
    		alertDialog.setTitle("Error");
    		alertDialog.setMessage("You need at least 1 Point of Interest and GPS Location to upload a walk!");
    		alertDialog.show();
    	}
    }
    
	/* (non-Javadoc)
	 * This method is overriden to set isFinished to true.
	 * We use this to determine that when the user closes the next
	 * alert dialog that the activity should finish and the user
	 * should be returned to the main app activity.
	 * 
	 * @see uk.ac.aber.group14.controller.IUploadFinishNotify#setFinished()
	 */
	@Override
	public void setFinished() {
		isFinished = true;
	}
	
	/* (non-Javadoc)
	 * We override this to tell when an alert dialog has been dismissed.
	 * If isFinished is true then we call "finish()" and exit.
	 * if isFinished is false then we set "isUploading" to false (this
	 * is because if we were given an alert dialog then we are not
	 * currently uploading as we have just informed the user that either
	 * they cannot upload, the upload worked, or the upload failed.
	 * 
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public void onDismiss(DialogInterface dialog) {
		if(isFinished)
		{
			finish();
		} else {
			isUploading = false;
		}
	}
	
	/* (non-Javadoc)
	 * This method is overriden in order to store local variables
	 * which need to persist, as well as the latitude/longitude to be
	 * displayed onscreen.
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
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
	
	/* (non-Javadoc)
	 * This is used to the current instance of this activity
	 * for the next instance after a configuration change.
	 * This allows us to bring the AsyncTask to the next instance
	 * and then update its dialog variables. This prevents window
	 * leaks and null pointers associated with dialogs with no visible
	 * parents.
	 * 
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}
	
	/**
	 * This is a simple get method to return the AsyncTask.
	 * Used to relay the AsyncTask to future instances of ourself.
	 * @return
	 */
	public WalkUploader getWalkUploader() {
		return walkUploader;
	}
}
