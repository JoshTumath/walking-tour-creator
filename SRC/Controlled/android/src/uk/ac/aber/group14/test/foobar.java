package uk.ac.aber.group14.test;
import uk.ac.aber.group14.R;
import uk.ac.aber.group14.viewer.MainAppActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class foobar extends 
   ActivityInstrumentationTestCase2<MainAppActivity> {
private TextView result;
   public foobar(String name) {
      super("uk.ac.aber.group14.test", MainAppActivity.class);
   }
   @Override
   protected void setUp() throws Exception {
      super.setUp();
      MainAppActivity mainApp = getActivity();
      result = (TextView) mainApp.findViewById(R.id.ConfirmAction);
   }
   public void testGPS(){
      System.out.println("please fucking work");
   }
}
