
/*
 * @(#) WalkUploader.java 1.0 2014-01-31
 *
 * Copyright (c) 2014 Aberystwyth University.
 * All rights reserved.
 *
 */package uk.ac.aber.group14.controller;

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

/**
 * This class is used as a means of making http requests without blocking
 * the ui thread, as is required by Android.
 * It is given an IWalkController and uses this to compile the JSON walk,
 * send it, and then return a true or false value for whether it was
 * sent successfully.
 * This must be provided with an AlertDialog, ProgressDialog, and IFinishNotify
 * in order to function correctly. 
 * @author Group14
 *
 */
public class WalkUploader extends AsyncTask<IWalkController, Integer, Boolean> {
   private ProgressDialog progressDialog;
   private AlertDialog alertDialog;
   private static final String uploadAddress = "http://jakemaguire.co.uk/projects/wtc/upload.php";
   /*private static final String uploadAddress = "http://lem0n.net/~tiggy/store.php";*/
   private IUploadFinishNotify finishNotify;

   /**
    * This is used to set the WalkUploader's dialogs and IFinishNotify.
    * 
    * @param progressDialog The ProgressDialog to show
    * @param alertDialog The AlertDialog to show
    * @param finishNotify The IFinishNotify to notify of successful upload
    */
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
   
   /**
    * This method is overriden in order to show the progress dialog
    * (if it is not null) before attempting to compile and upload the walk
    * @see android.os.AsyncTask#onPreExecute()
    */
   @Override
   protected void onPreExecute() {
      super.onPreExecute();
      if(progressDialog != null) {
         progressDialog.show();   
      }
   }
   
   /**
    * This method calls uploadWalk to compile and upload the
    * walk on a separate thread
    * @see uk.aber.ac.uk.group14.controller.WalkUploader#uploadWalk(IWalkController walkController)
    * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
    */
   @Override
   protected Boolean doInBackground(IWalkController... params) {
      return uploadWalk(params[0]);
   }
   
   /**
    * This method is overridden in order to dismiss the progress
    * dialog and display the alert dialog to the user, telling them
    * whether or not the upload succeeded or not.
    * If the upload was successful, then the IFinishNotify's "setFinished()"
    * method is also called.
    * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
    */
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
   
   /**
    * This method is used to compile and upload the walk.
    * It uses the IJsonPackager to compile the walk, and then
    * uses http to send the walk as a String in post data.
    * @param walkController
    * @return
    */
   public boolean uploadWalk(IWalkController walkController) {
      boolean uploadSuccess=true;
      String walk = walkController.compileWalk();
      /*Log.i("WTC", "\nCompiled walk:\n" + walk + "\n\n");*/
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
