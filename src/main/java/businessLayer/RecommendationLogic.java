package businessLayer;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.*;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import data.DatabaseConnection;
/*
 * RecommendationLogic class contains all the business logic for the application 
 */
public class RecommendationLogic {

	public String giveRecommendations(String customerId){
		Block<Document> printBlock = new Block<Document>() {
		       @Override
		       public void apply(final Document document) {
		           System.out.println(document.toJson());
		       }
		};
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("User");
		System.out.println("Your last searched restaurants");
		coll.find(and(eq("userName",customerId)))
		.projection(fields(include("restaurants"),excludeId()))
		.forEach(printBlock);		
		BasicDBObject searchQuery = new BasicDBObject();
	    searchQuery.put("userName",customerId);
	    String cuisine = "";
	    MongoCursor<Document> cursor = coll.find(searchQuery).iterator();  
	    try {
	    	 while (cursor.hasNext()) {
	    		 cuisine = cursor.next().getString("cuisines");
	    	 }
	    } 
	    catch(Exception e) {
	    	 System.out.println("******");
	    }
	    finally {
	        cursor.close();
	    }
	    return cuisine;
	}
	
	@SuppressWarnings("resource")
	public String getCity(){
		Scanner input = new Scanner(System.in);
		int flag =0;
		String city = "";
		while(flag!=1) {		
		System.out.println("Please select your location \n College Station \n Bryan \n Campus");
		city = input.nextLine().toLowerCase();
		if((city.equalsIgnoreCase("Bryan")) || (city.equalsIgnoreCase("College Station")) || (city.equalsIgnoreCase("Campus"))) {
			flag =1;
		}
		else {
			System.out.println("Please enter a correct location value!");
		}
		}
		return city;
	}
	

	@SuppressWarnings("resource")
	public String getCuisineType(String city) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("business");
		Scanner input = new Scanner(System.in);
		Bson filter = Filters.eq("city", city);
		int flag =0;
		String cuisine = "";
		DistinctIterable<String> lCuisineType = coll.distinct("cuisine_type", String.class);
		lCuisineType.filter(filter);
		while (	flag!=1) {
	        System.out.println("Please select an option from available cuisine types");
			for (String cuisineType : lCuisineType) {
			    System.out.println(cuisineType.toUpperCase());
			}
			cuisine = input.nextLine().toLowerCase();
		
			for (String cuisineTypeNew : lCuisineType) {
			    if(cuisineTypeNew.toUpperCase().equalsIgnoreCase(cuisine)) {
			    		flag =1;
			    }
			}
			if(0==flag){
				System.out.println("Please enter a correct cuisine value!");
			}
		}
		return cuisine;
	}

	@SuppressWarnings("resource")
	public void getRestuarantSuggestion(String city,String cuisine, String customerId) {
		Block<Document> printBlock = new Block<Document>() {
		       @Override
		       public void apply(final Document document) {
		           System.out.println(document.toJson());
		       }
		};
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("business");
		Scanner input = new Scanner(System.in);
		 int	flag =0; String restaurantIndex = ""; String choice = "";
			while(flag!=1) {
					System.out.println("Suggestions for you:: ");
					coll.find(and(eq("city", city),eq("cuisine_type", cuisine)))
						.projection(fields(include("restaurant_number","name","stars","cuisine_type"),excludeId()))
						.forEach(printBlock);
					System.out.println("Select any Restaurant Number from the list");
					restaurantIndex = input.nextLine();
					coll.find(and(eq("city", city),eq("cuisine_type", cuisine),eq("restaurant_number",restaurantIndex)))
					.projection(fields(include("name","stars","cuisine_type","price","phone","parking","wifi"),excludeId()))
					.forEach(printBlock);
					System.out.println("Are you satisfied with your restaurant choice? \n Yes \n No");
					choice = input.nextLine().toLowerCase();
					int flag1= 0;
						while(flag1!=1) {
						if(choice.equalsIgnoreCase("Yes")) {
							flag=1; 
							flag1=1;
							normalCourseForRestaurantSuggestion(cuisine, customerId, restaurantIndex, coll,databaseConnection);
						}
						else if(choice.equalsIgnoreCase("No")){
							alternateCourseForRestaurantSuggestion(city, cuisine,customerId);
						}
						else {
							System.out.println("Please enter a correct value!");
						}
					}
			}
	}
	
	public void normalCourseForRestaurantSuggestion(String cuisine, String customerId, String restaurantIndex, MongoCollection<Document> coll,DatabaseConnection databaseConnection) {
		BasicDBObject searchQuery = new BasicDBObject();
	    searchQuery.put("restaurant_number",restaurantIndex);
	    String restaurant = "";
	    MongoCursor<Document> cursor = coll.find(searchQuery).iterator();  
	    try {
	    	 while (cursor.hasNext()) {
	    		 restaurant = cursor.next().getString("name");  
	    	 }
	    } 
	    catch(Exception e) {
	    	System.out.println("*****");
	    }
	    finally {
	        cursor.close();
	    }
		MongoCollection<Document> collection = databaseConnection.getConnection().getCollection("User");
		collection.updateOne(
                eq("userName", customerId), combine(set("cuisines", cuisine), set("restaurants", restaurant)));
	}
	
	@SuppressWarnings("resource")
	public void alternateCourseForRestaurantSuggestion(String city, String cuisine, String customerId) {
		Scanner input = new Scanner(System.in);
		System.out.println("Do you want to change the restaurant? \n Yes \n No");
		String choiceRestuarant= input.nextLine().toLowerCase();
		String choiceCuisine = "";
		if(choiceRestuarant.equalsIgnoreCase("No")) {
			System.out.println("Do you want to change the cuisine type? \n Yes \n No");
			choiceCuisine = input.nextLine().toLowerCase();
			if(choiceCuisine.equalsIgnoreCase("Yes")) {
			     cuisine =  getCuisineType(city);
			     getRestuarantSuggestion(city,cuisine,customerId);
			}
			else {
                  city = getCity();
                  cuisine =  getCuisineType(city);
                  getRestuarantSuggestion(city,cuisine,customerId);
			}	
		}
	}
	
	
}
