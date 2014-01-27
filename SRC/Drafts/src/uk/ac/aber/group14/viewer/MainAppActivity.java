package uk.ac.aber.group14.viewer;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;
import uk.ac.aber.group14.viewer.WalkCreatorActivity;
import uk.ac.aber.group14.viewer.WalkDetailsActivity;

public class MainAppActivity extends Activity {
	
	
	LocationManager manager;
	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled")
		.setCancelable(false)       
		.setNegativeButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}






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
