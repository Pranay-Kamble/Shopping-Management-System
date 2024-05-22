import java.util.InputMismatchException;import java.util.Scanner;import Classes.Customer;import Classes.Product;import Classes.Admin;import Exceptions.InvalidPhoneNumberException;import SQL.SQLHandler;import java.util.ArrayList;public class Main {    private static Scanner console = new Scanner(System.in);    private static Customer customer = new Customer();    private static final Product product = new Product();    private static final Admin admin = new Admin();    private static final SQLHandler sql = new SQLHandler();    private static void exit(){        System.out.println("Thank you for visiting our Store.");        System.gc();        console.close();        System.exit(0);    }    private static int initialScreenOptions(){        System.out.println("\n-----------Welcome to our Store----------");        console = new Scanner(System.in);        int choice = 0;        while(choice < 1 || choice > 3){            System.out.println("1)Customer Login\n2)Admin Login\n3)Exit");            System.out.print("Enter your choice: ");            try {                choice = console.nextInt();                if (choice < 1 || choice > 3){                    throw new InputMismatchException("Input not Valid");                }            }            catch(InputMismatchException exception){                System.out.println("Please enter a valid input!\n");                if (console.hasNextLine()) {                    console.nextLine();                }            }        }        return choice;    }    private static int customerLoginMenu(){        console = new Scanner(System.in);        int choice = -1;        System.out.println("\n----------Customer Login Page----------");        do {            System.out.println("1)Already Existing Customer Login\n2)New Customer Sign Up\n3)Previous Screen\n4)Exit");            System.out.print("Enter your choice: ");            try {                choice = console.nextInt();                if (choice < 1 || choice > 4) {                    throw new InputMismatchException("Input not Valid");                }            }            catch (InputMismatchException exception) {                System.out.println("Please enter a valid input!\n");                if (console.hasNextLine()) {                    console.nextLine();                }            }        } while(choice < 1 || choice > 4);        return choice;    }    /**     * This function is used to ask for a valid input according to the Menu Options.     * @return Returns the valid choice between 1 to 5     */    private static int showMenu() {        System.out.println("\n----------Menu Page----------");        int choice = -1;        console = new Scanner(System.in);        do {            if (choice != -1)                System.out.println("\nPlease enter a valid choice!");            System.out.println("1) See Items in the store\n2) Add Items to cart by Product ID\n3) View Items in cart\n4) Billing\n5) Logout\n6) Exit\n");            System.out.print("Enter your choice: ");            if (console.hasNextInt())                choice = console.nextInt();            if (console.hasNextLine())                console.nextLine();        } while(choice < 1 || choice > 6);        return choice;    }    public static void main (String[] args) {        while (true) {            switch (Main.initialScreenOptions()) {                case 1: //Customer Login                    int customerLoginChoice;                    do {                        customerLoginChoice = Main.customerLoginMenu();                        switch (customerLoginChoice) {                            case 2:                                if (!(sql.customerSignUp()))                                    break;                            case 1:  //Already Existing Customer Login                                String phoneNumber = "";                                String password = "";                                Customer login = null;                            {                                do {                                    try {                                        System.out.print("Enter your Mobile Number: ");                                        if (console.hasNextLine())                                            phoneNumber = console.next();                                        console.nextLine();                                        if (!(SQLHandler.isValidPhoneNumber(phoneNumber)))                                            throw new InvalidPhoneNumberException("Not a valid Mobile Number.");                                    } catch (InvalidPhoneNumberException exception) {                                        System.out.println("Please enter a valid Phone number!");                                    } catch (Exception exception) {                                        System.out.println("Parent Class Exception has occurred!");                                        System.out.println("Exception Message: " + exception.getMessage());                                    }                                    if (!sql.isUserPresent(phoneNumber)) {                                        System.out.println("Account Not Found");                                        break;                                    }                                } while (!(SQLHandler.isValidPhoneNumber(phoneNumber)));                                if (!sql.isUserPresent(phoneNumber))                                    break;                                do {                                    try {                                        System.out.print("Enter your password (without spaces): ");                                        if (console.hasNextLine())                                            password = console.nextLine();                                    } catch (Exception exception) {                                        System.out.println("Parent Class Exception has occurred!");                                        System.out.println("Exception Message: " + exception.getMessage());                                    }                                } while (password == null || password.isEmpty());                                login = sql.customerLogin(phoneNumber, password);                            }                            if (login == null) {                                System.out.println("Incorrect Password");                            } else {                                customer = login;                                customer.display();                                System.out.println("Login Successful.\n");                                int showMenuChoice;                                do {                                    showMenuChoice =  Main.showMenu();                                    ArrayList<Product> cart = new ArrayList<Product> ();                                    cart.add(new Product("p001","product",201.99,100));                                    switch (showMenuChoice) {                                        case 1:  //See items in the store                                            try {                                                sql.showItems();                                            } catch (Exception exception) {                                                System.out.println("Exception Message: " + exception.getMessage());                                            }                                            break;                                        case 2: //Add items to cart by product id;                                            //cart.add();                                        case 3: //View items in the cart                                            if (cart.isEmpty()) {                                                System.out.println("Cart is Empty");                                                break;                                            }                                            else {                                                for (int i = 0; i<cart.size(); i++) {                                                    System.out.println(cart.get(i));                                                }                                            }                                            break;                                        case 4: //Billing                                            //cart.generateBill();                                        case 5: //Logout                                            System.out.println("Logged Out Successfully.");                                            break;                                        case 6: //Exit                                                Main.exit();                                    }                                } while (showMenuChoice != 5);                            }                            case 3: //Previous                                break;                            case 4: //Exit                                Main.exit();                        }                    }while(customerLoginChoice != 3);                    break;                case 2:  //Admin Login                    System.out.println("2nd option received. I should call the adminLogin() method");                    break;                case 3:  //Exit                    Main.exit();            }        }    }}