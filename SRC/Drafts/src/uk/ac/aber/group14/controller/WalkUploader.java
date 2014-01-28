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
import uk.ac.aber.group14.viewer.MainAppActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WalkUploader extends AsyncTask<String, Integer, Boolean> {
	private ProgressDialog progressDialog;
	private AlertDialog.Builder messageDialogBuilder;
	private static final String uploadAddress = "http://jakemaguire.co.uk/projects/wtc/upload.php";
	/*private static final String uploadAddress = "http://google.co.uk/";*/

    public void setDialogs(ProgressDialog progressDialog, AlertDialog.Builder messageDialogBuilder) {
        this.progressDialog = progressDialog;
        this.messageDialogBuilder = messageDialogBuilder;
    }
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.setMessage("Please wait whilst the walk is uploaded...");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		return uploadWalk(params[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
        progressDialog.dismiss();
		if(result == false) {
			messageDialogBuilder.setTitle("Error");
			messageDialogBuilder.setMessage("Upload failed. Please try again.");
		} else {
			messageDialogBuilder.setTitle("Success");
			messageDialogBuilder.setMessage("Upload Successful.");
		}
		messageDialogBuilder.create().show();
	}
	
	public boolean uploadWalk(String walk) {
		boolean uploadSuccess=true;
		Log.v("WTC", "\n\n=== JSON WALK===\n\n" + walk + "\n\n================");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(uploadAddress);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("posttourdata", walk));
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
