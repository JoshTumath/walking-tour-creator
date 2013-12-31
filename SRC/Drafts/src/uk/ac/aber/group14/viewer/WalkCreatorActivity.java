package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class WalkCreatorActivity extends Activity {
	IWalkController walkController;
	
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walk_creator, menu);
		return true;
	}
	
	public void cancelWalk(View view) {
		finish();
	}
	
	public void addLocation(View view) {
		Intent locationIntent = new Intent(this, LocationActivity.class);
		startActivityForResult(locationIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK) {
			IPointOfInterest point = (PointOfInterest)data.getSerializableExtra("pointOfInterest");
			walkController.addPOI(point);
		}
	}

}
