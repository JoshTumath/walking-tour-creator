package uk.ac.aber.group14.controller;

/**
 * This interface is used to allow our WalkUploader AsyncTask
 * to talk back to the calling Activity as long as it implements
 * this interface. This is in order to signify when the file has
 * been uploaded and the activity can call "finish()".
 * 
 * @author Group14
 *
 */
public interface IUploadFinishNotify {

	/**
	 * This should be used to tell our class that it is finished.
	 */
	public void setFinished();
}
