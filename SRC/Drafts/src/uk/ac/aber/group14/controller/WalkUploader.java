package uk.ac.aber.group14.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.JsonPackager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

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
   private static final String uploadAddress = "http://jakemaguire.co.uk/projects/wtc/upload.php";
   private ProgressDialog progressDialog;
   private AlertDialog alertDialog;
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
      boolean returnValue = false;
      try {
         return uploadWalk(params[0]);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      return returnValue;
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
    * @throws IOException 
    * @throws MalformedURLException 
    */
   public boolean uploadWalk(IWalkController walkController) throws MalformedURLException, IOException {
      boolean uploadSuccess=true;
      String walk = walkController.compileWalk();
      List<NameValuePair> nameValuePairs;
      HttpClient httpClient = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(uploadAddress);
      
      //NameValurPairs are used for post data such that name=value in the POST
      nameValuePairs = new ArrayList<NameValuePair>();
      nameValuePairs.add(new BasicNameValuePair("tourdata", walk));
      try {
         httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
      } catch (UnsupportedEncodingException e) {
         uploadSuccess=false;
      }
      
      HttpResponse response = null;
      try {
         response = httpClient.execute(httpPost);
      } catch (ClientProtocolException e) {
         uploadSuccess = false;
      } catch (IOException e) {
         uploadSuccess = false;
      }
      
      /* 200 is the http response code for "OK" - if we get anything but this
         then we have not managed to upload anything properly */
      if(response == null || response.getStatusLine().getStatusCode() != 200) {
         uploadSuccess = false;
      }
      
      /*HttpURLConnection urlConnection;
      urlConnection = (HttpURLConnection) new URL(uploadAddress).openConnection();
      
      urlConnection.setDoOutput(true);
      urlConnection.setRequestMethod("POST");
      
      urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
      
      
      //Default is UTF-8 for android, so this is quite predictable
      char[] jsonWalk = walkController.memSafeCompileWalk().toCharArray();
      IPointOfInterest[] pois = walkController.getPOIs();
      String pictures[] = pois[0].getPictures();
      int nextNull = indexOfNextNull(jsonWalk, 0); 
      int lastNull=0, poi=0, picture=0;
      for(nextNull=0; nextNull<jsonWalk.length; lastNull=indexOfNextNull(jsonWalk, nextNull+1)) {
         String urlEncodedChunk = String.copyValueOf(jsonWalk, lastNull, nextNull - lastNull);
         printWriter.write(urlEncodedChunk);
         String urlEncodedBase64Image = JsonPackager.EncodePhotoUpgrade(pictures[picture]);
         printWriter.write(urlEncodedBase64Image);
         if(picture < pictures.length - 1) {
            poi++;
            picture = 0;
         } else {
            picture++;
         }
      }
      printWriter.close();
      
      Scanner inStream = new Scanner(urlConnection.getInputStream());
      StringBuilder responseBuilder = new StringBuilder();
      while(inStream.hasNextLine()) {
         responseBuilder.append(inStream.nextLine());
      }
      String response = responseBuilder.toString();
      int responseStart = response.indexOf(' ')+1;
      int responseCode = Integer.valueOf(response.substring(responseStart, responseStart+3));
      if(responseCode != 200) {
         uploadSuccess = false;
      }*/
      
      return uploadSuccess;
   }
   
   private int indexOfNextNull(char[] string, int lastIndex) {
      int index;
      for(index = lastIndex; index < string.length; index++) {
         if(string[index] == '0') {
            break;
         }
      }
      return lastIndex;
   }
}
