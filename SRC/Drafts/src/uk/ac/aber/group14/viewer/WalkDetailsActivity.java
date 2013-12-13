package uk.ac.aber.group14.viewer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import uk.ac.aber.group14.R;

public class WalkDetailsActivity extends Activity {

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
		if(validInput())
		{
			Intent newWalk = new Intent(this, WalkDetailsActivity.class);
			String name = ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString();
			String shortDescription = ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString();
			String longDescription = ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString();
			newWalk.putExtra("name", name);
			newWalk.putExtra("short_description", shortDescription);
			newWalk.putExtra("long_description", longDescription);
			startActivity(newWalk);
		}
		else
		{
			
		}
	}
	
	public void cancel(View view) {
		finish();
	}
	
	public boolean validInput()	{
		boolean isValid = true;
		EditText name = (EditText)findViewById(R.id.walkDetailsNameEdit);
		EditText shortDescription = (EditText)findViewById(R.id.walkDetailsSDEdit);
		EditText longDescription = (EditText)findViewById(R.id.walkDetailsLDEdit);
		if(name.getText().toString() == "" ||
				shortDescription.getText().toString() == "" ||
				longDescription.getText().toString() == "") {
			isValid = false;
		}

		return isValid;
	}
}
