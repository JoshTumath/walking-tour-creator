package uk.ac.aber.group14.model;

/**
 * This interface is used to convert an IWalk to a JSON String
 * @author Group14
 *
 */
public interface IJsonPackager {
   
   /**
    * This takes an IWalk and converts it into a JSON String
    * @param w The walk which will be converted
    * @return A String containing the walk represented as a JSON object
    */
   public String JSONify(IWalk w);
   
   public String memSafeJSONify(IWalk w);
   
}
