package data;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
/* DataImport class establishes connection
 *  with the database and then imports data from csv file*/
public class DataImport {

	public void importData() throws FileNotFoundException {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("business");
		MongoCollection<Document> collection = databaseConnection.getConnection().getCollection("User");
		try {
				boolean businessExists = databaseConnection.getConnection().listCollectionNames()
					    .into(new ArrayList<String>()).contains("business");
				if (!businessExists) {
						try (BufferedReader in = new BufferedReader(new FileReader("db/updatedBusiness.csv"));) {
							String line;
							while ((line = in.readLine()) != null) {
								String[] var = line.split(",");
								Document newBusiness = new Document();
								newBusiness.append("business_id", var[0]);
								newBusiness.append("review_count", var[1]);
								newBusiness.append("star", var[2]);
								newBusiness.append("name", var[3]);
								newBusiness.append("address", var[4]);
								newBusiness.append("neighborhood", var[5]);
								newBusiness.append("city", var[6]);
								newBusiness.append("state", var[7]);
								newBusiness.append("postal_code", var[8]);
								if (var.length > 15) {
									newBusiness.append("cuisine_type", var[9]);				
								newBusiness.append("price", var[10]);
								newBusiness.append("phone", var[11]);
								newBusiness.append("parking", var[12]);
								newBusiness.append("wifi", var[13]);
								newBusiness.append("payment", var[14]);
								newBusiness.append("restaurant_number", var[15]);
								}
								coll.insertOne(newBusiness);
							}
						}
				
				}		
				
				boolean userExists = databaseConnection.getConnection().listCollectionNames()
					    .into(new ArrayList<String>()).contains("User");
				if (!userExists) {
						try (BufferedReader in = new BufferedReader(new FileReader("db/customer.csv"));) {
							String line;
							while ((line = in.readLine()) != null) {
								String[] var = line.split(",");
								Document newBusiness = new Document();
								if (var.length > 7) {
								newBusiness.append("customerName", var[0]);
								newBusiness.append("userName", var[1]);
								newBusiness.append("password", var[2]);
								newBusiness.append("address", var[3]);
								newBusiness.append("phoneNumber", var[4]);
								newBusiness.append("eMail", var[5]);
								newBusiness.append("restaurants", var[6]);
								newBusiness.append("cuisines", var[7]);
								}
								collection.insertOne(newBusiness);
							}
						}
				
				}		
		}
	 catch (Exception e) {
		 e.printStackTrace();
		System.out.println(e.toString());
	}
		finally {
			databaseConnection.closeConnection();
		}
	}
}
