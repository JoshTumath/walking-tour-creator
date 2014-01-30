package uk.ac.aber.group14.viewer;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.viewer.WalkCreatorActivity;
import uk.ac.aber.group14.viewer.WalkDetailsActivity;

/**
 * This activity is shown to the user when they launch the app.
 * It has the app's logo and a single button to create a walk.
 * Once the button is clicked it launches the walk details activity.
 * If data is received back from this then a new walk is created
 * and WalkCreatorActivity is launched.
 * 
 * @author Group14
 * @editor/commenter tht5
 *
 */
public class MainAppActivity extends Activity {
	
	
	LocationManager manager;
		//Pop up that happens only if the user does not have GPS enabled 
	 private void buildAlertMessageNoGps() {
		    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Your GPS seems to be disabled")
		           .setCancelable(false)       
		           .setNegativeButton("ok", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}

	
	/* (non-Javadoc)
	 * This is called when the activity is created. We use this to
	 * initialize the LocationManager "manager"
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_app);
		manager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_app, menu);
		return true;
	}

	/**
	 * This method is called by the create walk button.
	 * If the GPS is enabled then the walk details activity
	 * is launched.
	 * @param view
	 */
	public void createWalk(View view) {

		if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
			Intent newWalkDetails = new Intent(this, WalkDetailsActivity.class);
			startActivityForResult(newWalkDetails, 1);
		} // Checks if the GPS logger on the phone is on
		else{
			buildAlertMessageNoGps();
			//Pop up that says "GPS is not enabled, please enable GPS to continue"
		}
	}
	
     /* (non-Javadoc)
	  * This method is used to obtain the walk's details (name,
	  * short description, long description) from the walk details
	  * activity. If it did not return RESULT_OK, then we do nothing.
	  * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	  */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data ){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK) {
			Intent newWalkCreator = new Intent(this, WalkCreatorActivity.class);
			newWalkCreator.putExtra("name", data.getStringExtra("name"));
			newWalkCreator.putExtra("shortDescription", data.getStringExtra("shortDescription"));
			newWalkCreator.putExtra("longDescription", data.getStringExtra("longDescription"));
			startActivity(newWalkCreator);
		}
	}
}
