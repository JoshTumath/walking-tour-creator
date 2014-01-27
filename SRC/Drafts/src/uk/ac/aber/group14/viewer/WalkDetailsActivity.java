package uk.ac.aber.group14.viewer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.controller.IWalkController;
import uk.ac.aber.group14.controller.WalkControllerPrototype;

public class WalkDetailsActivity extends Activity {

	private final int short_desc_len = 100;
	private final int long_desc_len = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walk_details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walk_details, menu);
		return true;
	}

	public void createWalk(View view) {
		if(validInput()) {
			Intent output = new Intent();
			output.putExtra("name", ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString());
			output.putExtra("shortDescription", ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString());
			output.putExtra("longDescription", ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString());
			setResult(Activity.RESULT_OK, output);
			finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.WalkDetailsInvalidInputMessage);
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void cancel(View view) {
		setResult(Activity.RESULT_CANCELED, new Intent());
		finish();
	}
	
	public boolean validInput()	{
		String name = ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString();
		String shortDescription = ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString();
		String longDescription = ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString();
		boolean isValid = (name.length() > 0 &&
				shortDescription.length() < short_desc_len &&
				longDescription.length() < long_desc_len);

		return isValid;
	}
}
