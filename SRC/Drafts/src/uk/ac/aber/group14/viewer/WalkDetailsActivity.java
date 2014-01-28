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
import java.lang.String;
import java.util.regex.Pattern;

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
			
			else if (validChecker == 2){
				builder.setMessage(R.string.WalkDetailsInvalidInputName);
			}
			
			else if (validChecker == 3){
				builder.setMessage(R.string.WalkDetailsInvalidInputShort);
			}
			
			else if (validChecker == 4){
				builder.setMessage(R.string.WalkDetailsInvalidInputLong);
			}
			
			else if (validChecker == 5){
				builder.setMessage(R.string.WalkDetailsInvalidInputCharacters);
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
	//code the prevents back button
		 public void onBackPressed() {
		     // TODO Auto-generated method stub
			 //buildAlertMessageBackMessage();
		  //   super.onBackPressed();
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
		
		else if(name.length() > name_desc_len){
			result = 2;
		}
		
		else if(shortDescription.length() > short_desc_len){
			result = 3;
		}
		
		else if(longDescription.length() > long_desc_len){
			result = 4;
		}
		/*
		else if(!Pattern.matches("/\\s|[a-zA-Z]|\\d|\\.*",name)){
			result = 5;
		}
		*/

		return result;
	}
}		
