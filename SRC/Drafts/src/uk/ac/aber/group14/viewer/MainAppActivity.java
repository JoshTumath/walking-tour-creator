package uk.ac.aber.group14.viewer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;

public class MainAppActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_app);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_app, menu);
		return true;
	}

	public void createWalk(View view) {
		Intent newWalkDetails = new Intent(this, WalkDetailsActivity.class);
		startActivityForResult(newWalkDetails, 1);
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
