import java.util.InputMismatchException;
import java.util.Scanner;
import Classes.Customer;
import Classes.Product;
import Classes.Admin;
import Exceptions.InvalidPhoneNumberException;
import SQL.SQLHandler;

public class Main {
    private static Scanner console = new Scanner(System.in);
    private static Customer customer = new Customer();
    private static Product product = new Product();
    private static Admin admin = new Admin();
    private static SQLHandler sql = new SQLHandler();

    private static void exit(){
        System.out.println("Thank you for visiting our Store.");
        console.close();
        System.exit(0);
    }

    private static int initialScreenOptions(){
        System.out.println("-----------Welcome to our Store----------");
        console = new Scanner(System.in);
        int choice = 0;
        while(choice < 1 || choice > 3){
            System.out.println("1)Customer Login\n2)Admin Login\n3)Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = console.nextInt();
                if (choice < 1 || choice > 3){
                    throw new InputMismatchException("Input not Valid");
                }
            }
            catch(InputMismatchException exception){
                System.out.println("Please enter a valid input!\n");
                if (console.hasNextLine()) {
                    console.nextLine();
                }
            }
        }
        return choice;
    }

    private static int customerLoginMenu(){
        console = new Scanner(System.in);
        int choice = -1;
        System.out.println("\n----------Customer Login Page----------");
        do {
            System.out.println("1)Already Existing Customer Login\n2)New Customer Sign Up\n3)Previous Screen\n4)Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = console.nextInt();
                if (choice < 1 || choice > 4) {
                    throw new InputMismatchException("Input not Valid");
                }
            }
            catch (InputMismatchException exception) {
                System.out.println("Please enter a valid input!\n");
                if (console.hasNextLine()) {
                    console.nextLine();
                }
            }
        } while(choice < 1 || choice > 4);
        return choice;
    }

    /**
     * This function is used to ask for a valid input according to the Menu Options.
     * @return Returns the valid choice between 1 to 5
     */

    private static int showMenu() {
        System.out.println("----------Menu Page----------");
        int choice = -1;
        console = new Scanner(System.in);

        do {
            if (choice != -1)
                System.out.println("\nPlease enter a valid choice!");
            System.out.println("1)See items in the store\n2)Add items to cart by Product ID\n3)View Items in cart\n4)Billing\n5)Exit\n");
            System.out.print("Enter your choice: ");
            if (console.hasNextInt())
                choice = console.nextInt();

            if (console.hasNextLine())
                console.nextLine();
        } while(choice < 1 || choice > 5);
        return choice;
    }

    public static void main (String[] args) {

        while (true) {
            switch (Main.initialScreenOptions()) {

                case 1: //Customer Login
                    switch (Main.customerLoginMenu()) {

                        case 2: //New Customer SignUp

                            if (!(sql.customerSignUp()))
                                break;


                        case 1:  //Already Existing Customer Login
                            String phoneNumber = "";
                            String password = "";
                            Customer login;
                            do {

                                do {

                                    try {
                                        System.out.print("Enter your Mobile Number: ");
                                        if (console.hasNextLine())
                                            phoneNumber = console.next();
                                        console.nextLine();

                                        if (!(SQLHandler.isValidPhoneNumber(phoneNumber)))
                                            throw new InvalidPhoneNumberException("Not a valid Mobile Number.");
                                    } catch (InvalidPhoneNumberException exception) {
                                        System.out.println("Please enter a valid Phone number!");
                                    } catch (Exception exception) {
                                        System.out.println("Parent Class Exception has occurred!");
                                        System.out.println("Exception Message: " + exception.getMessage());
                                    }

                                } while (!(SQLHandler.isValidPhoneNumber(phoneNumber)));

                                do {

                                    try {
                                        System.out.print("Enter your password (without spaces): ");
                                        if (console.hasNextLine())
                                            password = console.nextLine();
                                    } catch (Exception exception) {
                                        System.out.println("Parent Class Exception has occurred!");
                                        System.out.println("Exception Message: " + exception.getMessage());
                                    }

                                } while (password == null || password.isEmpty());

                                login = sql.customerLogin(phoneNumber, password);
                            } while (!(login.isValid())); //!(login.isValid()) --Replace this i have edited for testing purpose

                            customer = login;
                            customer.display(); //testing purpose
                            System.out.println("Login Successful.");

                            do {
                                switch (Main.showMenu()) {
                                    case 1:  //See items in the store
                                        try {
                                            sql.showItems();
                                        } catch (Exception exception) {
                                            System.out.println("Exception Message: " + exception.getMessage());
                                        }

                                    case 2: //Add items to cart by product id;

                                        //cart.add();

                                    case 3: //View items in the cart

                                        //cart.display()

                                    case 4: //Billing

                                        //cart.generateBill();

                                    case 5: //Exit
                                        Main.exit();
                                }

                            }while (true);

                        case 3: //Previous
                            break;

                        case 4: //Exit
                            Main.exit();
                    }
                    break;
                case 2:  //Admin Login
                    System.out.println("2nd option received. I should call the adminLogin() method");
                    break;
                case 3:  //Exit
                    Main.exit();
            }
        }
    }
}