package uk.ac.aber.group14.viewer;
import uk.ac.aber.group14.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
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

}
