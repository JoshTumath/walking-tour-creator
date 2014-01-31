package tests;

import org.junit.Test;
import android.location.Location;
import uk.ac.aber.group14.model.JsonPackager;
import uk.ac.aber.group14.model.PointOfInterest;
import uk.ac.aber.group14.model.Walk;
import junit.framework.TestCase;

public class TestWalkCreator extends TestCase {
	Walk w;

	protected void setUp() throws Exception {
		super.setUp();
		this.w = new Walk("Test Walk", "Test Short Description", "Long Description Test aaaaaa");
		
		Location l = new Location("Tester");
		l.setLatitude(52.152);
		l.setLongitude(-4.222);
		w.addLocation(l);
		
		l = new Location("Tester");
		l.setLatitude(52.160);
		l.setLongitude(-4.250);
		w.addLocation(l);
		
		l = new Location("Tester");
		l.setLatitude(52.167);
		l.setLongitude(-4.260);
		PointOfInterest poi = new PointOfInterest(l);
		poi.setName("Test POI 1");
		poi.setDescription("Test Desc 1");
		w.addPointOfInterest(poi);
		
		l = new Location("Tester");
		l.setLatitude(52.177);
		l.setLongitude(-4.240);
		poi = new PointOfInterest(l);
		poi.setName("Test POI 2");
		poi.setDescription("Test Desc 2");
		w.addPointOfInterest(poi);
	}

	@Test
	public void testJSONify() {
		JsonPackager jp = new JsonPackager();
		System.out.println(jp.JSONify(this.w));
	}
}
