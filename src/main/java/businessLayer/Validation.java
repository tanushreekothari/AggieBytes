package businessLayer;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.DatabaseConnection;
/* 
 * Validation class checks validates customer demographics
 *  before creating new customer in the database
 */
public class Validation {

	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
	
	
	public boolean isValidUserName(String userName) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("User");
		BasicDBObject searchQuery = new BasicDBObject();
	    searchQuery.put("userName",userName);
	    boolean uFlag = true;
	    MongoCursor<Document> cursor = coll.find(searchQuery).iterator();  
	    try {
	    	 while (cursor.hasNext()) {
	        if(cursor.next().getString("userName").equals(userName))
	        {
	           	uFlag = false;
	        }
	    	 }
	    } 
	    catch(Exception e) {
	    	 System.out.println("******");
	    }
	    finally {
	        cursor.close();
	    }
		return uFlag;
 }

	public boolean isValidPhoneNumber(String phoneNumber) {
		 String ePattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
	        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
	        java.util.regex.Matcher m = p.matcher(phoneNumber);
	        return m.matches();
 }
	
	public boolean isValidPassword(String password, String userName) {
		 boolean valid = true;
         if (password.length() > 15 || password.length() < 8)
         {
                 System.out.println("Password should be less than 15 and more than 8 characters in length.");
                 valid = false;
         }
         if (password.indexOf(userName) > -1)
         {
                 System.out.println("Password Should not be same as user name");
                 valid = false;
         }
         String upperCaseChars = "(.*[A-Z].*)";
         if (!password.matches(upperCaseChars ))
         {
                 System.out.println("Password should contain atleast one upper case alphabet");
                 valid = false;
         }
         String lowerCaseChars = "(.*[a-z].*)";
         if (!password.matches(lowerCaseChars ))
         {
                 System.out.println("Password should contain atleast one lower case alphabet");
                 valid = false;
         }
         String numbers = "(.*[0-9].*)";
         if (!password.matches(numbers ))
         {
                 System.out.println("Password should contain atleast one number.");
                 valid = false;
         }
         String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
         if (!password.matches(specialChars ))
         {
                 System.out.println("Password should contain atleast one special character");
                 valid = false;
         }
		return valid;
 }
	
	
}
