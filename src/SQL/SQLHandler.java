package SQL;

import java.sql.*;
import Classes.Customer;
import Classes.Product;
import Exceptions.CustomerAlreadyPresent;
import Exceptions.InvalidPhoneNumberException;
import java.util.Scanner;
import java.util.ArrayList;

public class SQLHandler {

    private static final String URL = "jdbc:mysql://localhost:3306/Shopping_Management";
    private static final String User = "root";
    private static final String Password = "password";
    private static Connection connection = null;

    private static ArrayList<Customer> customersData = new ArrayList<>();
    private static ArrayList<Customer> updatedCustomersData = new ArrayList<>();
    private static ArrayList<Product> productsData = new ArrayList<>();
    private static ArrayList<Product> updatedProductsData = new ArrayList<>();

    static {
        try {
            connection = DriverManager.getConnection(URL,User,Password);
            SQLHandler.getCustomerData();
            SQLHandler.getProductData();
            System.out.println("DataBase Connection established Successfully");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed.");
        }
    }

    /**
     * Fetches data from the database containing details of Customers and stores it in ArrayList
     * Using this we can reduce the number of calls to the database
     * We make one call to the database and store the data in ArrayList
     */

    private static void getCustomerData()  {
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT ID,NAME,AGE,PHONENUMBER,PASSWORD,MONEYSPENT FROM USERS";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                customersData.add(new Customer(result.getString(1), result.getString(2), result.getInt(3), result.getString(4), result.getString(5), result.getDouble(6)));
            }

            for (Customer customer : customersData) {
                updatedCustomersData.add(customer);
            }
        }catch (SQLException e) {
            System.out.println("SQLException occurred in SQLHandler.getCustomerData(). Exception message: " + e.getMessage());
        }
    }

    /**
     * Handles all the updation of data in user database, it can add new data and also update existing data
     */

    private static void updateCustomerDB()  {
        String addQuery = "INSERT INTO USERS VALUES (?,?,?,?,?,?)";
        String updateQuery = "UPDATE USERS SET NAME = ? , AGE = ? , PHONENUMBER = ? , PASSWORD = ? , MONEYSPENT = ? WHERE ID = ?";
        PreparedStatement addStatement = null;
        PreparedStatement updateStatement = null;
        try {
            addStatement = connection.prepareStatement(addQuery);
            updateStatement = connection.prepareStatement(updateQuery);
        } catch (SQLException e) {
            System.out.println("SQLException occurred in SQLHandler.updateCustomerDB(). Exception message: " + e.getMessage());
        }

        if (customersData.size() < updatedCustomersData.size()){  //This means new customer have been added
            try {
                for (int i = customersData.size(); i < updatedCustomersData.size(); ++i) {
                    addStatement.setString(1, updatedCustomersData.get(i).getId());
                    addStatement.setString(2,updatedCustomersData.get(i).getName());
                    addStatement.setInt(3,updatedCustomersData.get(i).getAge());
                    addStatement.setString(4,updatedCustomersData.get(i).getPhoneNumber());
                    addStatement.setString(5,updatedCustomersData.get(i).getPassword());
                    addStatement.setDouble(6,updatedCustomersData.get(i).getMoneySpent());
                    addStatement.executeUpdate();
                }
            }catch (SQLException e) {
                System.out.println("SQLException occurred in SQLHandler.updateCustomerDB() near addition of new customer. Exception message: " + e.getMessage());
            }
        }

        for (int i = 0 ; i < customersData.size(); ++i) {
            if (!customersData.get(i).equals(updatedCustomersData.get(i))) {
                try{
                    updateStatement.setString(1, updatedCustomersData.get(i).getName());
                    updateStatement.setInt(2,updatedCustomersData.get(i).getAge());
                    updateStatement.setString(3,updatedCustomersData.get(i).getPhoneNumber());
                    updateStatement.setString(4,updatedCustomersData.get(i).getPassword());
                    updateStatement.setDouble(5,updatedCustomersData.get(i).getMoneySpent());
                    updateStatement.setString(6,updatedCustomersData.get(i).getId());
                    updateStatement.executeUpdate();
                }catch (SQLException e) {
                    System.out.println("SQLException occurred in SQLHandler.updateCustomerDB() near updation of existing customer. Exception message: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles all the updation of data in items database, it can add new data and also update existing data
     */

    private static void updateProductDB() {
        String addQuery = "INSERT INTO ITEMS VALUES (?,?,?,?)";
        String updateQuery = "UPDATE ITEMS SET NAME = ? , PRICE = ? , QUANTITY = ? WHERE ID = ?";
        PreparedStatement addStatement = null;
        PreparedStatement updateStatement = null;
        try {
            addStatement = connection.prepareStatement(addQuery);
            updateStatement = connection.prepareStatement(updateQuery);
        } catch (SQLException e) {
            System.out.println("SQLException occurred in SQLHandler.updateProductDB(). Exception message: " + e.getMessage());
        }

        if (productsData.size() < updatedProductsData.size()){  //This means new customer have been added
            try {
                for (int i = customersData.size(); i < updatedCustomersData.size(); ++i) {
                    addStatement.setString(1, updatedProductsData.get(i).getId());
                    addStatement.setString(2,updatedProductsData.get(i).getName());
                    addStatement.setDouble(3,updatedProductsData.get(i).getPrice());
                    addStatement.setInt(4,updatedProductsData.get(i).getQuantity());
                    addStatement.executeUpdate();
                }
            }catch (SQLException e) {
                System.out.println("SQLException occurred in SQLHandler.updateProductDB() near addition of new customer. Exception message: " + e.getMessage());
            }
        }

        for (int i = 0 ; i< productsData.size(); ++i) {
            if (!productsData.get(i).equals(updatedProductsData.get(i))) {
                try{
                    updateStatement.setString(1, updatedProductsData.get(i).getName());
                    updateStatement.setDouble(2,updatedProductsData.get(i).getPrice());
                    updateStatement.setInt(3,updatedProductsData.get(i).getQuantity());
                    updateStatement.setString(4,updatedProductsData.get(i).getId());
                    updateStatement.executeUpdate();
                }catch (SQLException e) {
                    System.out.println("SQLException occurred in SQLHandler.updateCustomerDB() near updation of existing customer. Exception message: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Fetches data from the database containing details of Products and stores it in ArrayList
     * Using this we can reduce the number of calls to the database
     * We make one call to the database and store the data in ArrayList
     */

    private static void getProductData()  {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT ID,NAME,PRICE,QUANTITY FROM ITEMS";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                productsData.add(new Product(result.getString(1), result.getString(2), result.getDouble(3), result.getInt(4)));
            }
        }catch (SQLException e) {
            System.out.println("SQLException occurred in SQLHandler.getProductData(). Exception message: " + e.getMessage());
        }
    }

    /**
     * Checks for validity using regex and returns a boolean
     * @param phoneNumber A string to check for validity as phoneNumber
     * @return boolean
     */

    public static boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("[1-9][0-9]{9}");
    }

    public static int getInitialCustomers(){
        return customersData.size();
    }

    /**
     * Returns a boolean value after checking whether the user is present or not
     * @param phoneNumber A valid phoneNumber to check
     * @return boolean
     */
    public boolean isUserPresent(String phoneNumber) {
        SQLHandler.productsData.clear();
        SQLHandler.getCustomerData();  //First fetch the data from database
        for (Customer i: customersData){  //Then check for presence
            if (i.getPhoneNumber().equals(phoneNumber))
                return true;
        }
        return false;
    }

    private boolean addCustomer(Customer customer){
        SQLHandler.getCustomerData();
        updatedCustomersData.add(customer);
        SQLHandler.updateCustomerDB();
        return true;
    }

    /**
     * Displays the Products after fetching them from the database.
     */
    public void showItems(){
        SQLHandler.productsData.clear();
        SQLHandler.getProductData(); //Fetch the products data
        System.out.println("+-----+-----------------+------------+------------+");
        System.out.printf("%-5S | %-15S | %-10S | %-10S |\n","ID","Name","Price","Quantity");
        for (int j = 0; j < productsData.size(); j++) {
            Product i = productsData.get(j);
            System.out.printf("%-5s | %-15s | %-10.2f | %-10s |\n", i.getId(), i.getName(), i.getPrice(), i.getQuantity());
        }
        System.out.println("+-----+-----------------+------------+------------+");
    }

    public boolean customerSignUp(){
        String name;
        int age = -1;
        String phoneNumber = null;
        String password = null;
        String confirm = null;

        Scanner console = new Scanner(System.in);

            System.out.print("Enter your name: ");
            name = console.nextLine();

            do {

                System.out.print("Enter your age: ");
                if (console.hasNextInt())
                    age = console.nextInt();

                else {
                    System.out.println("Please enter a valid age");
                    if (console.hasNextLine())
                        console.nextLine();
                }


            }while (age < 1);
            console.nextLine();

            do {
                System.out.print("Enter the phone number: ");
                try{
                    if (console.hasNext())
                        phoneNumber = console.next();

                    if (isUserPresent(phoneNumber))
                        throw new CustomerAlreadyPresent("Customer Phone Number is already present.");

                    if (!(isValidPhoneNumber(phoneNumber)))
                        throw new InvalidPhoneNumberException("Please enter a valid 10 digit phone number");
                }
                catch (CustomerAlreadyPresent | InvalidPhoneNumberException exception){
                    System.out.println("Exception: " + exception.getMessage());
                }
                catch (Exception exception){
                    System.out.println("In Parent Exception of phone number");
                    System.out.println(exception.getMessage());
                }
            } while (!(phoneNumber.matches("[1-9][0-9]{9}")));

            console.nextLine();

            do {
                System.out.print("Create your password: ");
                if (console.hasNextLine()){
                    password = console.nextLine();
                }
                if (password == null)
                    continue;
                System.out.print("Confirm password: ");
                if (console.hasNextLine())
                    confirm = console.nextLine();

                if (!(password.equals(confirm)))
                    System.out.println("Confirm password correctly!");
            }while (!(password.equals(confirm)));

            for (int i = 0; i<password.length() ; ++i){
                if (password.charAt(i) == ' '){
                    System.out.println("Spaces found in your password. Removing all the spaces. Please remember your password without spaces.");
                    break;
                }
            }

            name = name.replace(" ","_");
            password = password.replace(" ", "");
            Customer cust = new Customer(name,age,phoneNumber, password);

            if (addCustomer(cust)) {
                System.out.println("Your account has been created. Now Please Login.");
                return true;
            }
            else {
                System.out.println("Some exception has occurred. Please try again later.  CustomerSignup function");
                return false;
            }
    }

    public Customer customerLogin(String mobileNumber, String password) {   //Incomplete still need to implement
        for (Customer i: customersData){
            if (i.getPhoneNumber().equals(mobileNumber) && i.getPassword().equals(password)){
                return i;
            }
        }
        return null;
    }  //Will take mobile number and password, and check if user is present and password matches then it should return the
    // customer object by constructing the customer object by extracting data from database
    // and print

    public static void main(String [] args){
        SQLHandler.getProductData();

        SQLHandler sql = new SQLHandler();
        sql.showItems();
    }
}