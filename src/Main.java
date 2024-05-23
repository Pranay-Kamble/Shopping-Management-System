import SQL.SQLHandler;import java.util.InputMismatchException;import java.util.Scanner;import Classes.Customer;import Classes.Product;import Classes.Admin;import Exceptions.InvalidPhoneNumberException;import java.util.ArrayList;public class Main {    private static final SQLHandler sql = new SQLHandler();    private static Scanner console = new Scanner(System.in);    private static Customer customer = new Customer();    private static Product product = new Product();    private static final Admin admin = new Admin();    private static void exit(){        System.out.println("Thank you for visiting our Store.");        System.gc();        console.close();        System.exit(0);    }    private static int initialScreenOptions(){        System.out.println("\n-----------Welcome to our Store----------");        console = new Scanner(System.in);        int choice = 0;        while(choice < 1 || choice > 3){            System.out.println("1)Customer Login\n2)Admin Login\n3)Exit");            System.out.print("Enter your choice: ");            try {                choice = console.nextInt();                if (choice < 1 || choice > 3){                    throw new InputMismatchException("Input not Valid");                }            }            catch(InputMismatchException exception){                System.out.println("Please enter a valid input!\n");                if (console.hasNextLine()) {                    console.nextLine();                }            }        }        return choice;    }    private static int customerLoginMenu(){        console = new Scanner(System.in);        int choice = -1;        System.out.println("\n----------Customer Login Page----------");        do {            System.out.println("1)Already Existing Customer Login\n2)New Customer Sign Up\n3)Previous Screen\n4)Exit");            System.out.print("Enter your choice: ");            try {                choice = console.nextInt();                if (choice < 1 || choice > 4) {                    throw new InputMismatchException("Input not Valid");                }            }            catch (InputMismatchException exception) {                System.out.println("Please enter a valid input!\n");                if (console.hasNextLine()) {                    console.nextLine();                }            }        } while(choice < 1 || choice > 4);        return choice;    }    /**     * This function is used to ask for a valid input according to the Menu Options.     * @return Returns the valid choice between 1 and 5     */    private static int showMenu() {        System.out.println("\n----------Menu Page----------");        int choice = -1;        console = new Scanner(System.in);        do {            if (choice != -1)                System.out.println("\nPlease enter a valid choice!");            System.out.println("1) See Items in the store\n2) Add Items to cart by Product ID\n3) View Items in cart\n4) Edit Items in the cart\n5) Billing\n6) Logout\n7) Exit\n");            System.out.print("Enter your choice: ");            if (console.hasNextInt())                choice = console.nextInt();            if (console.hasNextLine())                console.nextLine();        } while(choice < 1 || choice > 7);        return choice;    }    public static void main (String[] args) {        while (true) {            switch (Main.initialScreenOptions()) {                case 1:                    int customerLoginChoice;                    do {                        customerLoginChoice = Main.customerLoginMenu();                        switch (customerLoginChoice) {                            case 2:                                if (!(sql.customerSignUp()))                                    break;                            case 1:                                String phoneNumber = "";                                String password = "";                                Customer login;                            {                                do {  //TODO if phone number is already present in database, code should jump out to login page, but it is asking for password                                    try {                                        System.out.print("Enter your Mobile Number: ");                                        if (console.hasNextLine())                                            phoneNumber = console.next();                                        console.nextLine();                                        if (!(SQLHandler.isValidPhoneNumber(phoneNumber)))                                            throw new InvalidPhoneNumberException("Not a valid Mobile Number.");                                    } catch (InvalidPhoneNumberException exception) {                                        System.out.println("Please enter a valid Phone number!");                                    } catch (Exception exception) {                                        System.out.println("Parent Class Exception has occurred!");                                        System.out.println("Exception Message: " + exception.getMessage());                                    }                                    if (!sql.isUserPresent(phoneNumber)) {                                        System.out.println("Account Not Found");                                        break;                                    }                                } while (!(SQLHandler.isValidPhoneNumber(phoneNumber)));                                if (!sql.isUserPresent(phoneNumber))                                    break;                                do {                                    try {                                        System.out.print("Enter your password (without spaces): ");                                        if (console.hasNextLine())                                            password = console.nextLine();                                    } catch (Exception exception) {                                        System.out.println("Parent Class Exception has occurred!");                                        System.out.println("Exception Message: " + exception.getMessage());                                    }                                } while (password == null || password.isEmpty());                                login = sql.customerLogin(phoneNumber, password);                            }                            if (login == null) {                                System.out.println("Incorrect Password");                            } else {                                customer = login;                                customer.display();                                System.out.println("Login Successful.\n");                                int showMenuChoice;                                ArrayList<Product> cart = new ArrayList<>();                                do {                                    showMenuChoice =  Main.showMenu();                                    switch (showMenuChoice) {                                        case 1:  //See items in the store                                            try {                                                sql.showItems();                                            } catch (Exception exception) {                                                System.out.println("Exception Message: " + exception.getMessage());                                            }                                            break;                                        case 2: //Add items to cart by product id;                                            String cartChoice = "yes";                                            String productId;                                            int quantity;                                            do {                                                System.out.print("Enter the product ID of the item:");                                                productId = console.nextLine();                                                if (!sql.isProductPresent(productId)){                                                    System.out.println("Product not found. Please check the entered Product ID.");                                                    continue;                                                }                                                if (sql.getProduct(productId).getQuantity() == 0){                                                    System.out.println("Sorry the product items selected are over. Please wait till restock.");                                                    break;                                                }                                                System.out.print("Enter the quantity of the item:");                                                if (console.hasNextInt()) {                                                    quantity = console.nextInt();                                                    if (quantity <= 0) {                                                        System.out.println("Please enter a positive value.");                                                        break;                                                    }                                                    if (sql.getQuantity(productId) < 0 || sql.getQuantity(productId) < quantity) {                                                        System.out.println("Quantity exceeds the limit.");                                                        break;                                                    }                                                    sql.sell(productId, quantity);  //Temporarily adds item to cart, will be permanently updated in DataBase after billing                                                    Product itemToBeAdded = sql.getProduct(productId);                                                    if (cart.contains(itemToBeAdded)) {                                                        int indexOfExistingProduct = cart.indexOf(itemToBeAdded);                                                        cart.get(cart.indexOf(itemToBeAdded)).setQuantity(quantity + cart.get(indexOfExistingProduct).getQuantity());                                                    }                                                    else                                                    {                                                        itemToBeAdded.setQuantity(quantity);                                                        cart.add(itemToBeAdded);                                                    }                                                    System.out.println("Added to cart.\n");                                                }                                                else{                                                    System.out.println("Please enter a valid quantity!");                                                }                                                if (console.hasNextLine()) //Clears input memory                                                    console.nextLine();                                                System.out.print("Do you want to add more items - Yes to add , any other key to exit: ");                                                cartChoice = console.nextLine();                                            }while (cartChoice.matches("(([yY])([eE])([sS]))|y|Y"));                                            break;                                        case 3: //View items in the cart                                            if (cart.isEmpty()) {                                                System.out.println("Cart is Empty");                                                break;                                            }                                            else {                                                System.out.println("Displaying items in the cart -");                                                System.out.println("+-------+---------------------------+------------+------------+");                                                System.out.printf("| %-5S | %-25S | %-10S | %-10S |\n","ID","Name","Price","Quantity");                                                for (Product value : cart) {                                                    System.out.println(value);                                                }                                                System.out.println("+-------+---------------------------+------------+------------+");                                            }                                            break;                                        case 4: //Edit items in the cart -->  important function                                             //TODO create menu to remove items from cart using productid and quantity                                            //cart.generateBill();                                        case 5: //Billing                                            double totalAmount = 0;                                            System.out.println("Billing section is to be implemented.");                                            System.out.println("Bill");                                            System.out.println("Name: " + customer.getName() + "\nID");                                            System.out.println("+-------+---------------------------+------------+------------+");                                            System.out.printf("| %-5S | %-25S | %-10S | %-10S | %-20S |\n","ID","Name","Price","Quantity","Total");                                            for (Product value : cart) {                                                System.out.print(value.toString());                                                totalAmount += (value.getQuantity() * value.getPrice();                                                System.out.printf("%-20S |\n",(value.getQuantity() * value.getPrice()));                                            }                                            System.out.println("+-------+---------------------------+------------+------------+");                                            break; //TODO  call the updateProductsDB function                                        case 6: //Logout                                            System.out.println("Logged Out Successfully.");                                            break;                                        case 7: //Exit                                                Main.exit();                                    }                                    if (showMenuChoice == 6 )                                        break;                                } while (showMenuChoice != 7);                            }                            case 3:                                break;                            case 4:                                Main.exit();                        }                    }while(customerLoginChoice != 3);                    break;                case 2:                    System.out.println("2nd option received. I should call the adminLogin() method");                    break;                case 3:                    Main.exit();            }        }    }}