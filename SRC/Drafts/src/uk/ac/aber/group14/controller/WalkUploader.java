package uk.ac.aber.group14.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class WalkUploader extends AsyncTask<IWalkController, Integer, Boolean> {
	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private static final String uploadAddress = "http://jakemaguire.co.uk/projects/wtc/upload.php";
	/*private static final String uploadAddress = "http://lem0n.net/~tiggy/store.php";*/
	private IUploadFinishNotify finishNotify;

    public void setDialogsAndNotify(ProgressDialog progressDialog,
    		AlertDialog alertDialog, IUploadFinishNotify finishNotify) {
        this.progressDialog = progressDialog;
        this.alertDialog = alertDialog;
        this.finishNotify = finishNotify;
        
		progressDialog.setMessage("Please wait whilst the walk is uploaded...");
		progressDialog.setIndeterminate(true);
		
		alertDialog.setTitle("Success");
		alertDialog.setMessage("Upload Successful.");
    }
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(progressDialog != null) {
			progressDialog.show();	
		}
	}
	
	@Override
	protected Boolean doInBackground(IWalkController... params) {
		return uploadWalk(params[0]);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
        progressDialog.dismiss();
		if(result == false) {
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Upload failed. Please try again.");
		} else {
			finishNotify.setFinished();
		}
		alertDialog.show();
	}
	
	public boolean uploadWalk(IWalkController walkController) {
		boolean uploadSuccess=true;
		String walk = walkController.compileWalk();
		Log.i("WTC", "\nCompiled walk:\n" + walk + "\n\n");
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(uploadAddress);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("tourdata", walk));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			Log.i("WTC", "Error attempting to encode URL: " + e.getLocalizedMessage());
			uploadSuccess=false;
		}
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess = false;
		} catch (IOException e) {
			Log.i("WTC", e.getLocalizedMessage());
			uploadSuccess = false;
		}
		
		if(response == null || response.getStatusLine().getStatusCode() != 200) {
			uploadSuccess = false;
		} else {
			try {
				Log.i("WTC", "\n\n====== RECEIVED DATA =====\n" +
						EntityUtils.toString(response.getEntity()) + 
						"\n======= END OF DATA ======\n\n");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return uploadSuccess;
	}
}