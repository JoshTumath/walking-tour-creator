package uk.ac.aber.group14.viewer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import uk.ac.aber.group14.R;
import java.lang.String;

/**
 * This class is used to set the details for a walk.
 * It allows the user to enter a name (with no spaces),
 * short description (< this.short_desc_len) and a long
 * description (<this.long_desc_len).
 * The user cannot continue without supplying at least
 * one character in each field.
 * 
 * @author Group14
 *
 */
public class WalkDetailsActivity extends Activity {

   private final int short_desc_len = 100;
   private final int long_desc_len = 1000;
   private final int name_desc_len = 255;
   
   /**
    * This method is used to create the Activity.
    * In here we check to see if there is a saved
    * instance state and load our variables from it if
    * it exists.
    * 
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_walk_details);
      
      if(savedInstanceState != null) {
         EditText name = (EditText)findViewById(R.id.walkDetailsNameEdit);
         EditText shortDescription = (EditText)findViewById(R.id.walkDetailsSDEdit);
         EditText longDescription = (EditText)findViewById(R.id.walkDetailsLDEdit);
         name.setText(savedInstanceState.getString("name"));
         shortDescription.setText(savedInstanceState.getString("shortDescription"));
         longDescription.setText(savedInstanceState.getString("longDescription"));
      }
      
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.walk_details, menu);
      return true;
   }

   /**
    * This method is used called by the create walk button.
    * It checks if the data entered by the user is valid and
    * adds that data to its output intent.
    * @param view
    */
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
   
   /**
    * This method is called by the cancel button.
    * It finishes the result with a Activity.RESULT_CANCELED
    * to let the calling Activity know that it was cancelled.
    * @param view
    */
   public void cancel(View view) {
      setResult(Activity.RESULT_CANCELED, new Intent());
      finish();
   }
   
       /**
        * This method is overriden in order to prevent
        * the back button from taking the user to the previos
        * activity
       * @see android.app.Activity#onBackPressed()
       */
      public void onBackPressed() {

       }
   
   /**
    * This method is used to validate all of the user's input.
    * It returns the following numbers depending on the input:
    *       0 - Valid
    *       1 - Empty name/descriptions 
    *       2 - Name too long
    *       3 - Short description too long
    *       4 - Long description too long
    * @return
    */
   public int validInput()   {
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
      
      else if(name.contains(" ")){
         result = 5;
      }
      

      return result;
   }
   
   /**
    * This method is overriden to store anything between configuration
    * changes. This is what allows us to keep the text boxes populated
    * when rotating the screen
    * 
    * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
    */
   @Override
   public void onSaveInstanceState(Bundle out) {
      String name = ((EditText)findViewById(R.id.walkDetailsNameEdit)).getText().toString();
      String shortDescription = ((EditText)findViewById(R.id.walkDetailsSDEdit)).getText().toString();
      String longDescription = ((EditText)findViewById(R.id.walkDetailsLDEdit)).getText().toString();
      out.putString("name", name);
      out.putString("shortDescription", shortDescription);
      out.putString("longDescription", longDescription);
   }
}      
