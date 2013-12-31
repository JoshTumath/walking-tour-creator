package uk.ac.aber.group14.model;

import android.os.Parcelable;

public interface IPointOfInterest extends Parcelable{
	
	public void setName(String name);
	
	public void setShortDescription(String desc);
	
	public void setlongDescription(String desc);
	
	public void addPicture(android.graphics.Bitmap image);
	
	public String getName();
	
	public String getShortDescription();
	
	public String getLongDescription();
	
	public android.graphics.Bitmap[] getPictures();
	
	public android.location.Location getLocation();
}
