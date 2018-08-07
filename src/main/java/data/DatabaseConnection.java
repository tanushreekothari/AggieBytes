package data;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
/*
 * Database Connection is a common class that has establishes and closes calls to database
 */
public class DatabaseConnection {
	MongoClient databaseClient = new MongoClient("127.0.0.1", 27017);
	MongoDatabase db = databaseClient.getDatabase("YelpProject");
	
	public MongoDatabase getConnection(){
		return db;
	}
	
	public void closeConnection() {
		databaseClient.close();
	}
	
}
