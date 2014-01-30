package uk.ac.aber.group14.viewer;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.PointOfInterest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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


/**
 * This activity is shown to the user when they add a new
 * point of interest. It allows them to enter a name, description,
 * and optionally take a picture with the camera intent.
 * The information is added to an Intent as the activity finishes. 
 * 
 * @author Group14
 *
 */
public class LocationActivity extends Activity {
	private Button addPicture;
	private String picture;
	final Context context = this;
	Location location;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final String changePictureText = "Change picture";
	static final int nameMaxLength = 255;
	static final int descMaxLength = 1000;
	static enum InputValidity {VALID, NONAME, NODESC, LONGNAME, LONGDESC};
	
	/* (non-Javadoc)
	 * This method is overriden to load any data from the
	 * savedInstanceState which was saved on a configuration change.
	 * This allows us to re-populate the text boxes and use the
	 * pictue that was caputed by the user before the change.
	 * s
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
	
	/**
	 * This method is used to check whether or not the input entered by
	 * the user is valid. This checks if both the name & description
	 * are not empty.
	 * 
	 * @return boolean to show whether the input was valid (T) or not (F)
	 */
	public InputValidity validInput() {
		int locationNameLength = ((TextView) findViewById(R.id.locationNameEdit)).getText().length();
		int locationDescLength = ((TextView) findViewById(R.id.locationDescriptionEdit)).getText().length();
		InputValidity returnValue = InputValidity.VALID;
		if(locationNameLength == 0) {
			returnValue = InputValidity.NONAME;
		} else if(locationDescLength == 0) {
			returnValue = InputValidity.NODESC;
		} else if(locationNameLength >= nameMaxLength) {
			returnValue = InputValidity.LONGNAME;
		} else if(locationDescLength >= descMaxLength) {
			returnValue = InputValidity.LONGDESC;
		}
		
		return returnValue;
	}
	
	/**
	 * This method is called by the "add location" button.
	 * It checks if the input is valid and saves the data to
	 * an Intent if true.
	 * This data can be retrieved by the calling Intent.
	 * The Acitivity then closes with "finish()", assuming the data
	 * was valid.
	 * 
	 * @param view
	 */
	public void addLocation(View view) {//button add location in confirm
		InputValidity inputValidity = validInput();
		if(inputValidity == InputValidity.VALID) {
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
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String error;
			
			switch(inputValidity) {
				case NONAME : error = "No name given."; break;
				case NODESC : error = "No description given."; break;
				case LONGNAME : error = "Name must be less than " + 
						nameMaxLength + " characters."; break;
				case LONGDESC : error = "Description must be less than " + 
						descMaxLength + " characters."; break;
				default : error = "Error."; break;
			}
			
			builder.setTitle("Error");
			builder.setMessage(error);
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}
	
	/**
	 * This method is called by the cancel button, closing
	 * the Activity and returning an Activity.RESULT_CANCELED
	 * result.
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		setResult(Activity.RESULT_CANCELED, new Intent());
		finish();
	}
	

	/**
	 * This method is called by the "Add picture" button. It
	 * starts the camera intent.
	 * 
	 * @param view
	 */
	public void onClickPictureButton(View view) {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	/* (non-Javadoc)
	 * This method is overriden to listen for the camera Intent's result.
	 * If we receive a RESULT_OK and the request code was
	 * REQUEST_IMAGE_CAPTURE then we pass the url to getPath
	 * and set the picture to the returned file path String
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

	    	addPicture.setText(changePictureText);
	    	picture = getPath(data.getData());
	    }
	}

	/**
	 * This method takes a Uri and obtains the file path to
	 * the resource. It accesses the local MediaStore database,
	 * using the provided uri.
	 * The first entry is returned, giving us our file's path.
	 * @param uri The Uri object to find the path of
	 * @return The path to the resource in the filesystem
	 */
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
	
	 
	/* (non-Javadoc)
	 * This method is overriden in order to save the text box
	 * contents in the event of a configuration change (screen
	 * re-orientation etc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
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
