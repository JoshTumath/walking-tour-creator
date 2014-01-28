package uk.ac.aber.group14.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import uk.ac.aber.group14.R;
import uk.ac.aber.group14.model.IJsonPackager;
import uk.ac.aber.group14.model.JsonPackager;
import uk.ac.aber.group14.model.Walk;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WalkUploader extends AsyncTask<Walk, Integer, Boolean> {
	private ProgressDialog progressDialog;
	private Dialog messageDialog;
	private Context context;
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		messageDialog = new Dialog(context);
		progressDialog.setMessage("Please wait whilst the walk is uploaded...");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(Walk... params) {
		return uploadWalk(params[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		messageDialog.setContentView(R.layout.unimplemented);
		if(result == false) {
			messageDialog.setTitle("Error");
			TextView text = (TextView) messageDialog.findViewById(R.id.text);
			text.setText("Upload failed. Please try again.");
			Button dialogButton = (Button) messageDialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					messageDialog.dismiss();
					messageDialog.getOwnerActivity().finish();
				}
			});
		} else {
			messageDialog.setTitle("Success");
			TextView text = (TextView) messageDialog.findViewById(R.id.text);
			text.setText("Upload Successful.");
			Button dialogButton = (Button) messageDialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					messageDialog.dismiss();
				}
			});
			messageDialog.show();
		}
		messageDialog.show();
	}
	
	public boolean uploadWalk(Walk walk) {
		boolean uploadSuccess=true;
		IJsonPackager jsonPackager = new JsonPackager();
		Log.i("WTC", "Number of locations:	" + walk.getLocations().length + "\n" +
				"Number of points:	" + walk.getPointsOfInterest().length);
		String walkObject = jsonPackager.JSONify(walk);
		Log.i("WTC", "\n\n=== JSON WALK===\n\n" + walkObject + "\n\n================");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://jakemaguire.co.uk/projects/wtc/upload.php");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("posttourdata", walkObject));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			Log.i("WTC", "Error attempting to encode URL: " + e.getLocalizedMessage());
			uploadSuccess=false;
		}
		try {
			httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess=false;
		} catch (IOException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess=false;
		}
		return uploadSuccess;
	}

}
