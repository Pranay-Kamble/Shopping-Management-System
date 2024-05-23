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
    @SuppressWarnings("FieldMayBeFinal")
    private static ArrayList<Customer> customersData = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private static ArrayList<Customer> updatedCustomersData = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private static ArrayList<Product> productsData = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private static ArrayList<Product> updatedProductsData = new ArrayList<>();

    public static int initialCustomers;
    public static int initialProducts;
    /*
      Initialises the DataBase Connection
      and Retrieves any existing Data from the DataBase
     */

    static {
        try {
            connection = DriverManager.getConnection(URL,User,Password);
            SQLHandler.getCustomerData();
            SQLHandler.getProductData();
            initialCustomers = customersData.size();
            initialProducts = productsData.size();
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

    @SuppressWarnings("UseBulkOperation")
    private static void getCustomerData()  {
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT ID,NAME,AGE,PHONENUMBER,PASSWORD,MONEYSPENT FROM USERS";
            ResultSet result = statement.executeQuery(query);

            customersData.clear();
            while (result.next()) {
                customersData.add(new Customer(result.getString(1), result.getString(2), result.getInt(3), result.getString(4), result.getString(5), result.getDouble(6)));
            }
            if (customersData.size() > updatedCustomersData.size() || updatedCustomersData.isEmpty()) {

                for (Customer customer : customersData) {
                    updatedCustomersData.add(customer);
                }
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
                System.out.println("Printing customer data array");

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
        customersData.clear();
        updatedCustomersData.clear();
        productsData.clear();
        updatedProductsData.clear();
        getCustomerData();
        getProductData();

    }

    /**
     * Handles all the updation of data in items database, it can add new data and also update existing data
     */

    //WIll be used by admin
    @SuppressWarnings("unused")
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
                System.out.println("SQLException occurred in SQLHandler.updateProductDB() near addition of new Product. Exception message: " + e.getMessage());
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
                    System.out.println("SQLException occurred in SQLHandler.updateCustomerDB() near updation of existing Product. Exception message: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Fetches data from the database containing details of Products and stores it in ArrayList
     * Using this we can reduce the number of calls to the database
     * We make one call to the database and store the data in ArrayList
     */

    @SuppressWarnings("UseBulkOperation")
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
        if (productsData.size() > updatedProductsData.size()) {
//            updatedProductsData.clear();
            for (Product product : productsData) { //Also initialises the updatedProductsData
                updatedProductsData.add(product);
            }
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

    /**
     * Returns the number of customers who have created account
     * @return int
     */

    @SuppressWarnings("unused")
    public static int getNumberOfCustomers(){
        return customersData.size();
    }

    /**
     * Returns the number of products present in the database
     * @return int
     */
    @SuppressWarnings("unused")
    public static int getNumberOfProducts(){
        return productsData.size();
    }

    /**
     * Returns a boolean value after checking whether the user is present or not
     * @param phoneNumber A valid phoneNumber to check
     * @return Boolean
     */
    public boolean isUserPresent(String phoneNumber) {

        for (Customer i: customersData){  // check for presence
            if (i.getPhoneNumber().equalsIgnoreCase(phoneNumber))
                return true;
        }
        return false;
    }

    /**
     * Returns a boolean value after checking whether the Product is present or not
     * @param productId A valid phoneNumber to check
     * @return Boolean
     */

    public boolean isProductPresent(String productId){
        SQLHandler.productsData.clear();
        SQLHandler.getProductData();
        for (Product i: productsData){
            if (i.getId().equalsIgnoreCase(productId))
                return true;
        }
        return false;
    }

    /**
     * Temporarily sells the product and reduces its quantity as items go into cart, and we do not know if they are purchased or not
     * @param productId The productId of available product
     * @param quantity The purchase quantity
     */
    public void sell(String productId, int quantity){//Just updates the updatedProductId, after billing call the updateProductDB
        for (Product i: updatedProductsData){
            if (i.getId().equalsIgnoreCase(productId))
                i.setQuantity(i.getQuantity() - quantity);
        }
    }


    /**
     * If items are removed from the cart they need to go back into the shelf
     * @param productId The productId of product that is removed from the cart
     * @param quantity The quantity of product that is removed from the cart
     */
    public void add(String productId, int quantity){
        for (Product i: updatedProductsData){
            if (i.getId().equalsIgnoreCase(productId))
                i.setQuantity(i.getQuantity() + quantity);
        }
    }

    /**
     * Gives the quantity of product available
     * @param productId ProductId of a product
     * @return The available quantity
     */
    public int getQuantity(String productId){
        for (Product i: updatedProductsData){
            if (i.getId().equalsIgnoreCase(productId))
                return i.getQuantity();
        }
        return -1;
    }

    /**
     * Sends the product as a whole
     * @param productId ProductId of a product
     * @return The corresponding product object or null if not present
     */
    public Product getProduct(String productId){
        for (Product i: productsData){
            if(i.getId().equalsIgnoreCase(productId))
                return i;
        }
        return null;
    }

    /**
     * Tries to add a new user to the database
     * @param customer A new customer object
     * @return boolean depending on successful or unsuccessful
     */

    private boolean addCustomer(Customer customer){
        try {
            updatedCustomersData.add(customer);
            SQLHandler.updateCustomerDB();
            return true;
        }
        catch(Exception e){
            System.out.println("Exception occurred in SQLHandler.addCustomer(). Exception message: " + e.getMessage());
            return false;
        }
    }


    /**
     * Displays the Products after fetching them from the database.
     */

    public void showItems(){
        System.out.println("+-------+---------------------------+------------+------------+");
        System.out.printf("| %-5S | %-25S | %-10S | %-10S |\n","ID","Name","Price","Quantity");
        for (Product i : updatedProductsData) { //Using updatedProductsList because if item is in cart then some quantity is reduced which is not updated in original products data array
            System.out.printf("| %-5s | %-25s | %-10.2f | %-10s |\n", i.getId(), i.getName(), i.getPrice(), i.getQuantity());
        }
        System.out.println("+-------+---------------------------+------------+------------+");
    }

    /**
     * Handles and updates the money spent by the customer
     * @param customer  Customer for which we are updating the data
     */

    public void updateCustomerData(Customer customer){
        for (Customer i: updatedCustomersData){
            if (i.equals(customer)){
                i.addMoneySpent(customer.getMoneySpent());
            }
        }
        SQLHandler.updateCustomerDB();
    }


    /**
     * Handles the new customer signup process
     * @return boolean value representing successful or unsuccessful process
     */

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
                    return false;
                }
                catch (Exception exception){
                    System.out.println("In Parent Exception of phone number");
                    System.out.println(exception.getMessage());
                    return false;
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
            Customer newCustomer = new Customer(name,age,phoneNumber, password);

            if (addCustomer(newCustomer)) {
                System.out.println("Your account has been created. Now Please Login.");
                return true;
            }
            else {
                System.out.println("Some exception has occurred. Please try again later.  CustomerSignup function");
                return false;
            }
    }

    /**
     * Handles the functionality if user is already present
     * @param mobileNumber Mobile Number to check if user is present
     * @param password Corresponding password of the user
     * @return A customer object on successful login or else null
     */
    public Customer customerLogin(String mobileNumber, String password) {
        for (Customer i: customersData){
            if (i.getPhoneNumber().equals(mobileNumber) && i.getPassword().equals(password)){
                return i;
            }
        }
        return null;
    }
}