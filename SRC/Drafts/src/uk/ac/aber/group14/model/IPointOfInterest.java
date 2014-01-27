package uk.ac.aber.group14.model;

import android.os.Parcelable;

public interface IPointOfInterest extends Parcelable {
	
	public void setName(String name);
	
	public String getName();
	
	void setDescription(String desc);
	
	public String getDescription();
	
	public void addPicture(android.graphics.Bitmap image);
	
	public android.graphics.Bitmap getPicture();
	
	public android.location.Location getLocation();
	
}
