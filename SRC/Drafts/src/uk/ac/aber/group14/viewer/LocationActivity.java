package uk.ac.aber.group14.viewer;

import java.util.ArrayList;
import java.util.LinkedList;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class LocationActivity extends Activity implements Gallery.OnItemClickListener{
   static final int REQUEST_IMAGE_CAPTURE = 1;
   static final int nameMaxLength = 255;
   static final int descMaxLength = 1000;
   private static final int imageViewWidth = 100;
   private static final int imageViewHeight = 100;
   private final Context context = this;
   private ArrayList<String> pictures;
   private ArrayList<Bitmap> thumbnails;
   private Location location;
   private Gallery imageGallery;
   private GalleryAdapter galleryAdapter;
   private int deleteIndex;
   private boolean isDeleting;
   static enum InputValidity {VALID, NONAME, NODESC, LONGNAME, LONGDESC};
   
   /**
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
      
      imageGallery = (Gallery) this.findViewById(R.id.imageGallery);
      imageGallery.setOnItemClickListener(this);
      location = (Location) getIntent().getExtras().getParcelable("location");
      pictures = null;
      thumbnails = null;
      
      if(savedInstanceState != null)
      {
         String name = savedInstanceState.getString("name");
         String desc = savedInstanceState.getString("desc");
         ((TextView) findViewById(R.id.locationNameEdit)).setText(name);
         ((TextView) findViewById(R.id.locationDescriptionEdit)).setText(desc);
         pictures = savedInstanceState.getStringArrayList("pictures");
         thumbnails = savedInstanceState.getParcelableArrayList("thumbnails");
         location = savedInstanceState.getParcelable("location");
         isDeleting = savedInstanceState.getBoolean("isDeleting");
         deleteIndex = savedInstanceState.getInt("deleteIndex");
      } else {
         isDeleting = false;
         deleteIndex = 0;
      }
      
      if(pictures == null || thumbnails == null) {
         pictures = new ArrayList<String>();
         thumbnails = new ArrayList<Bitmap>();
      }
      
      galleryAdapter = new GalleryAdapter(this);
      imageGallery.setAdapter(galleryAdapter);
      
      if(isDeleting) {
         promptDeleteImage();
      }
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
         if(pictures.size() != 0) {
            pointOfInterest.addPictures(new LinkedList<String>(pictures));
         }
         Log.i("WTC", "Testing for valid location...");
         if(location != null) {
            Log.i("WTC", "Location not null.");
         } else {
            Log.i("WTC", "Location is null.");
         }
         Intent output = new Intent();//save
         
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
    * This method is used to delete the image at the currently set
    * "deleteIndex"
    */
   public void deleteImage() {
      if(deleteIndex < pictures.size() && deleteIndex < thumbnails.size()) {
         pictures.remove(deleteIndex);
         thumbnails.remove(deleteIndex);
         galleryAdapter.notifyDataSetChanged();
      }
      isDeleting=false;
   }
   
   /**
    * This method is used to prompt the user with an AlertDialog asking
    * them if they are sure they want to delete the image.
    */
   public void promptDeleteImage() {
      isDeleting = true;
      DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
         
         @Override
         public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE) {
               deleteImage();
            }
            dialog.dismiss();
         }
      };
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Delete?");
      builder.setMessage("Are you sure you want to delete this picture?");
      builder.setPositiveButton("Yes", deleteListener);
      builder.setNegativeButton("No", deleteListener);
      builder.setCancelable(false);
      builder.create().show();
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
   
   /**
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

         String picturePath = getPath(data.getData());
         Bitmap pictureThumb = (Bitmap) data.getExtras().get("data");
         pictures.add(picturePath);
         thumbnails.add(pictureThumb);
         galleryAdapter.notifyDataSetChanged();
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
   
    
   /**
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
      out.putParcelable("location", location);
      out.putBoolean("isDeleting", isDeleting);
      out.putInt("deleteIndex", deleteIndex);

      if(pictures.size() != 0 && thumbnails.size() != 0) {
         Log.i("WTC", "pictures and thumbnails exist");
         out.putStringArrayList("pictures", pictures);
         out.putParcelableArrayList("thumbnails", thumbnails);   
      }
   }
   
   /**
    * This adapter is used with the Gallary to draw the images on
    * the screen.
    * Each image is created by taking its position in the gallary and
    * using that as the index to lookup its image in the thumbnails arraylist 
    * @author Group14
    *
    */
   public class GalleryAdapter extends BaseAdapter {
      private Context context;
      
      public GalleryAdapter(Context context) {
         this.context = context;
      }

      @Override
      public int getCount() {
         return pictures.size();
      }

      @Override
      public Object getItem(int position) {
         return position;
      }

      @Override
      public long getItemId(int position) {
         return position;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         ImageView imageView = new ImageView(context);
         imageView.setImageBitmap(thumbnails.get(position));
         Gallery.LayoutParams params = new Gallery.LayoutParams(imageViewWidth,imageViewHeight);
         imageView.setLayoutParams(params);
         
         return imageView;
      }
   }

   /**
    * This catches when the user clicks on a picture, sets deleteIndex to the
    * position in the Gallery they clicked, and then prompts for deletion
    * with "promptDeleteImage()"
    * @see uk.ac.aber.group14.LocationActivity#promptDeleteImage()
    * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
    */
   @Override
   public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
      deleteIndex = position;
      promptDeleteImage();
   }
}
