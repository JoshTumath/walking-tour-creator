package uk.ac.aber.group14.viewer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
	private final int name_desc_len = 255;
	
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
		int validChecker = validInput();
		
		if(validChecker == 0) {
			Intent output = new Intent();
			output.putExtra("name", ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString());
			output.putExtra("shortDescription", ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString());
			output.putExtra("longDescription", ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString());
			setResult(Activity.RESULT_OK, output);
			finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			if(validChecker == 1){
				builder.setMessage(R.string.WalkDetailsInvalidInputMessage);
			}
			
			builder.setNeutralButton(android.R.string.ok,
		            new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            dialog.cancel();
		        }
		    });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void cancel(View view) {
		setResult(Activity.RESULT_CANCELED, new Intent());
		finish();
	}
	
	public int validInput()	{
		String name = ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString();
		String shortDescription = ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString();
		String longDescription = ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString();
		int result = 0;
		
		if(name.length() == 0 ||
				shortDescription.length() == 0 ||
				longDescription.length() == 0){
			result = 1;
		}
		
		
		
		//250 short
		//1000  long
		//255 name

		return result;
	}
}		
