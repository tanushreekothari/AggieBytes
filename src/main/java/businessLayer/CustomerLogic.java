package businessLayer;

import java.util.Scanner;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.DatabaseConnection;

/*
 * CustomerLogic class creates new user and authenticates old users
 */
public class CustomerLogic {

	String pass;
	public boolean createNewCustomer(Customer customer) {
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("User");
		Document document = new Document("customerName", customer.getCustomerName())
	               .append("userName",customer.getUserName())
	               .append("password", customer.getPassword() )
	               .append("address",customer.getAddress())
	               .append("phoneNumber", customer.getPhoneNumber())
	               .append("eMail", customer.geteMail())
	               .append("restaurants", " ")
	               .append("cuisines"," ");

	coll.insertOne(document);
		return true;
	}
	
	
	
	@SuppressWarnings("resource")
	public String[] checkCustomer() {
		String custFlag = "old";
		String customerId = "";
		String[] customerInfo = new String[2];
		int flag =0;
		Scanner input = new Scanner(System.in);
		while(flag!=1) {
		System.out.println("Are you a new user \n YES \n NO");
		String response= input.nextLine().toLowerCase();
		if(response.equalsIgnoreCase("yes")) {
			flag =1;
			customerInfo = inputCustomerInformation();
			customerId = customerInfo[0];
			custFlag = customerInfo[1];
		}
		else if(response.equalsIgnoreCase("no")) {
			flag =1;
			customerId= checkOldCustomer();
			}
		else {
			System.out.println("Please enter a valid response");
		}
	}
		String[] custInfo = new String[2];
		custInfo[0] = custFlag;
		custInfo[1] = customerId;
		return custInfo;
	}
	
	@SuppressWarnings("resource")
	public String checkOldCustomer() {
		String customerId = "";
		int passFlag =0;
		Scanner input = new Scanner(System.in);
		DatabaseConnection databaseConnection = new DatabaseConnection();
		MongoCollection<Document> coll = databaseConnection.getConnection().getCollection("User");
			while(passFlag!=1) {
			System.out.println("Please provide your User Id");
			String userId= input.nextLine();
			customerId = userId;
			System.out.println("Please provide your Password");
			pass= input.nextLine();
			BasicDBObject searchQuery = new BasicDBObject();
		    searchQuery.put("userName",userId);
		    MongoCursor<Document> cursor = coll.find(searchQuery).iterator();  
		    try {
		        while (cursor.hasNext()) {
		            if(cursor.next().getString("password").equals(pass)) {		            	
		            	System.out.println("User Authenticated");
		            	System.out.println("Welcome "+userId);
		            	passFlag =1 ;
		            }			            
		        }
		    } finally {
		        cursor.close();
		    }
		    if(passFlag==0) {
				System.out.println("User not Authenticated! \n Please Provide your credentials again.");
			}
		}			
			return customerId;
	}

	@SuppressWarnings("resource")
	public String[] inputCustomerInformation() {
		String custInfo[] = new String[2];
		Validation validation = new Validation();
		Customer customer = new Customer();
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your Name");
		customer.setCustomerName(input.nextLine());
		int vFlag = 0;
		while(vFlag!=1) {
			System.out.println("Enter your UserName");
			customer.setUserName(input.nextLine());
			vFlag = validateUserName(customer,validation);
		}
		custInfo[0] = customer.getUserName();
		validateOtherCustomerInformation(customer,validation);
		if(createNewCustomer(customer)) {
			System.out.println("New User Successfully created");
			System.out.println("Welcome "+customer.getUserName());
			custInfo[1] = "new";
		}
		return custInfo;
	}
	
	@SuppressWarnings("resource")
	public void validateOtherCustomerInformation(Customer customer, Validation validation) {
		Scanner input = new Scanner(System.in);
		int paFlag = 0;
		while(paFlag!=1) { 
			System.out.println("Enter your Password."
					+ "\n Password should be less than 15 and more than 8 characters in length."
					+ "\n Password Should not be same as user name"
					+ "\n Password should contain atleast one upper case alphabet"
					+ "\n Password should contain atleast one lower case alphabet"
					+ "\n Password should contain atleast one number."
					+ "\n Password should contain atleast one special character");
			customer.setPassword(input.nextLine());
			paFlag= validatePassword(customer,validation);
		}
		System.out.println("Enter your Address");
		customer.setAddress(input.nextLine());
		int pFlag = 0;
		while(pFlag!=1) {
			System.out.println("Enter your Phone Number");
			customer.setPhoneNumber(input.nextLine());
			pFlag = validatePhoneNumber(customer,validation);
		}
		int eMailFlag = 0;
		while(eMailFlag!=1) {
			System.out.println("Enter your eMail");
			customer.seteMail(input.nextLine());
			eMailFlag = validateEmailAddresss(customer,validation);
		}
	}
	
	public int validatePassword(Customer customer, Validation validation) {
		int paFlag =0;
		if(validation.isValidPassword(customer.getPassword(),customer.getUserName())) {
			paFlag = 1;
		}
		else {
			System.out.println("Invalid password");
			System.out.println("Enter your Password."
					+ "\n Password should be less than 15 and more than 8 characters in length."
					+ "\n Password Should not be same as user name"
					+ "\n Password should contain atleast one upper case alphabet"
					+ "\n Password should contain atleast one lower case alphabet"
					+ "\n Password should contain atleast one number."
					+ "\n Password should contain atleast one special character");
		}
		return paFlag;
	}
	
	public int validatePhoneNumber(Customer customer, Validation validation) {
		int pFlag =0;
		if(validation.isValidPhoneNumber(customer.getPhoneNumber())) {
			pFlag = 1;
		}
		else {
			System.out.println("Invalid Phone Number");
			System.out.println("Enter your Phone Number");
		}
		return pFlag;
	}
	
	public int validateEmailAddresss(Customer customer, Validation validation) {
		int eMailFlag =0;
		if(validation.isValidEmailAddress(customer.geteMail())) {
			eMailFlag = 1;
		}
		else {
			System.out.println("Invalid Email Address");
			System.out.println("Enter your eMail");
		}
		return eMailFlag;
	}
	
	public int validateUserName(Customer customer, Validation validation) {
		int vFlag =0;
		if(validation.isValidUserName(customer.getUserName())) {
			vFlag = 1;
		}
		else {
			System.out.println("Username already exists!");
			System.out.println("Enter your UserName");
		}
		return vFlag;
	}
}