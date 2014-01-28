package uk.ac.aber.group14.viewer;
import java.util.LinkedList;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
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
	private String picture;
	final Context context = this;
	Location location;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		location = (Location) getIntent().getExtras().getParcelable("location");
		
		/*addPicture = (Button) findViewById(R.id.addPicture);
		
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
			
			
		});*/
		
	}
	
	public boolean validInput() {
		String locationName = ((TextView) findViewById(R.id.locationNameEdit)).getText().toString();
		String locationDesc = ((TextView) findViewById(R.id.locationDescriptionEdit)).getText().toString();
		return (locationName.length() > 0 && locationDesc.length() > 0);
	}
	
	/*
	 * This method should be called by the "Add location button"
	 */
	public void addLocation(View view) {//button add location in confirm
		if(validInput()) {
			IPointOfInterest pointOfInterest = new PointOfInterest(location);
			String locationName = ((TextView) findViewById(R.id.locationNameEdit)).getText().toString();
			String locationDesc = ((TextView) findViewById(R.id.locationDescriptionEdit)).getText().toString();
			pointOfInterest.setName(locationName);
			pointOfInterest.setDescription(locationDesc);
			if(picture != null) {
				pointOfInterest.addPicture(picture);
			}
			
			Intent output = new Intent();//save
			
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

	public void onClickPictureButton(View view) {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	    	// TODO Save image, add it to list of images.
	    	((Button) this.findViewById(R.id.addPicture)).setText("Change picture");;
	    	picture = data.getDataString();
	    }
	}


}
