package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LocationActivity extends Activity {
	private Button addPicture;
	private String picture;
	final Context context = this;
	Location location;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final String changePictureText = "Change picture";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		addPicture = (Button) findViewById(R.id.addPicture);
		
		if(savedInstanceState != null)
		{
			String name = savedInstanceState.getString("name");
			String desc = savedInstanceState.getString("desc");
			((TextView) findViewById(R.id.locationNameEdit)).setText(name);
			((TextView) findViewById(R.id.locationDescriptionEdit)).setText(desc);
			if(savedInstanceState.getString("picture") != null)
			{
				picture = savedInstanceState.getString("picture");
				addPicture.setText(changePictureText);
				
			}
		}
		
		location = (Location) getIntent().getExtras().getParcelable("location");
		
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
			
			Log.i("WTC", "Adding POI:\nName:" + pointOfInterest.getName() +
					"\nPicture:" + pointOfInterest.getPicture());
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

	    	addPicture.setText(changePictureText);
	    	picture = getPath(data.getData());
	    }
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA }; // Use image media table
		Cursor cursor = managedQuery(uri, projection, null, null, null); // Although deprecated since API 11, we're working with API 8+
		/* Cursor allows random read-write to database (which in this case is
		 * the media database)
		 */
		startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);//Return the first entry, which will be our file
	}
	// sends infromtaion from location name/Description to a String 
	@Override
	public void onSaveInstanceState(Bundle out) {
		String name = ((EditText) findViewById(R.id.locationNameEdit)).getText().toString();
		String desc = ((EditText) findViewById(R.id.locationDescriptionEdit)).getText().toString();
		out.putString("name", name);
		out.putString("desc", desc);
		
		if(picture != null) {
			out.putString("picture", picture);	
		}
	}
}
