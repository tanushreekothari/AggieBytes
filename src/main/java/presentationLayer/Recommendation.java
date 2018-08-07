package presentationLayer;
import java.io.FileNotFoundException;
import businessLayer.CustomerLogic;
import businessLayer.RecommendationLogic;
import data.DataImport;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * Recommendation class is the first point of contact for our application. It contains the main class and has the 
 * calls for data import and business logic
 */
public class Recommendation {

	public static void main(String[] args) {
		Logger mongoLogger = Logger.getLogger( "com.mongodb" );
		mongoLogger.setLevel(Level.SEVERE);
		
		DataImport dataImport = new DataImport();
		try {
			dataImport.importData();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			String lastCuisine = "";
			CustomerLogic customerLogic = new CustomerLogic();
			String[] customerInfo = customerLogic.checkCustomer();
	       RecommendationLogic logic = new RecommendationLogic();
	       if(customerInfo[0].equals("old")) {
	    	    lastCuisine = logic.giveRecommendations(customerInfo[1]);
	       }
	       String city = logic.getCity();  
	       if(customerInfo[0].equals("old")) {
	    	   		logic.getRestuarantSuggestion(city, lastCuisine,customerInfo[1]);
	       }
		   else {
			   String cuisine = logic.getCuisineType(city);
			   logic.getRestuarantSuggestion(city, cuisine,customerInfo[1]);
	       }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
