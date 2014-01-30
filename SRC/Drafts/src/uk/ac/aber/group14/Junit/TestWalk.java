package uk.ac.aber.group14.Junit;

import java.util.LinkedList;

import uk.ac.aber.group14.model.IPointOfInterest;
import uk.ac.aber.group14.model.Walk;
import android.location.Location;
import junit.framework.TestCase;

public class TestWalk extends TestCase {
	
	public TestWalk(String name, String shortDescription, String longDescription) {
		super();
		
	}

	private String name;
	private String shortDescription;
	private String longDescription;
	private LinkedList<IPointOfInterest> points;
	private LinkedList<Location> locations;
	
	public void setUp(){
		name="walk";
		shortDescription="short walk Test";
		longDescription="long walk Test";
		
	}
	
	public void TestWalk(){
		String result="walk";
		assertEquals("walk", "walk");
	}

	public void TestgetLongDescription(){
	
	//	Walk.get;
	}
}
