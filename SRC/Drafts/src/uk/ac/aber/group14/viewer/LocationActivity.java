package uk.ac.aber.group14.viewer;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class LocationActivity extends Activity{
	
	private Button addPicture;
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		addPicture = (Button) findViewById(R.id.addPicture);
		
		addPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.unimplemented);
				dialog.setTitle("Unimplemented");
				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText("This feature has yet to be implemented.");
				Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	 
				dialog.show();
				
			}
			
			
		});
		
	}
	
	public boolean validInput() {
		//TODO: Add code here to validate the input
		boolean isValid = false;
		
		return isValid;
	}
	
	/*
	 * This method should be called by the "Add location button"
	 */
	public void addLocation(View view) {
		if(validInput()) {
			//TODO: Get the current location
			
			Location location = null;
			IPointOfInterest pointOfInterest = new PointOfInterest(location);

			//TODO: Set the point of interest up
			
			
			
			Intent output = new Intent();//save
			
			Bundle extras = output.getExtras(); 
			String tmp = extras.getString("myLocationalKey");
			Log.i("WTC", "testing addLocation");//used for testing
			output.putExtra("pointOfInterest", pointOfInterest);
			setResult(Activity.RESULT_OK, output);
			finish();//where you finish and output
		}
	}
	
	public void cancel(View view) {
		setResult(Activity.RESULT_CANCELED, new Intent());
		finish();
	}

}
