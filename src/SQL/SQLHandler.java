package SQL;

import java.sql.*;
import Classes.Customer;
import Exceptions.CustomerAlreadyPresent;
import Exceptions.InvalidPhoneNumberException;
import java.util.Scanner;

public class SQLHandler {

    private static final String URL = "jdbc:mysql://localhost:3306/Shopping_Management";
    private static final String User = "root";
    private static final String Password = "password";
    private static Connection connection = null;

    static {
        try {
            connection = DriverManager.getConnection(URL,User,Password);
            System.out.println("Connection established Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.matches("[1-9][0-9]{9}");
    }

    private boolean isUserPresent(String phoneNumber){//checks for the phoneNumber is the customer database and returns a boolean
        System.out.println("To be implemented");
        return false;
    }

    private boolean addCustomer(Customer customer){
    //use jdbc --> insert into customer values (customer.name , customer.age, customer.phoneNumber, customer.password)
        System.out.println("Should be implemented");
        return false;
    }

    public void showItems(){
        System.out.println("To be implemented");
        //jdbc query -> select productid,productname,quantity,price from customer;
    }

    public boolean customerSignUp(){   //Incomplete still need to implement
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
         // Now add this data to customer database
         //then call existing customer login

    }   //Will take name and other details in the body and check if mobile number is not present in db
 // if not present add new record to db

    public Customer customerLogin(String mobileNumber, String password) {   //Incomplete still need to implement
        return new Customer();
    }  //Will take mobile number and password, and check if user is present and password matches then it should return the
    // customer object by constructing the customer object by extracting data from database
    // and print

//    public static void main(String [] args){
//        SQLHandler test = new SQLHandler();
//
//        test.customerSignUp();
//    }
}