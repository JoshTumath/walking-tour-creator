package uk.ac.aber.group14.viewer;
import java.util.LinkedList;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
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
	static final String changePictureText = "Change picture";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		addPicture = (Button) this.findViewById(R.id.addPicture);
		
		if(savedInstanceState != null)
		{
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
		Log.i("WTC", "Uri given is " + uri.toString());
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		Log.i("WTC", "Return path is " + cursor.getString(column_index));
		return cursor.getString(column_index);
	}

	@Override
	public void onSaveInstanceState(Bundle out) {
		if(picture != null) {
			out.putString("picture", picture);	
		}
	}
}
