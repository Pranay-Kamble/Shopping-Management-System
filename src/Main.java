import SQL.SQLHandler;
import java.util.InputMismatchException;
import java.util.Scanner;
import Classes.Customer;
import Classes.Product;
import Classes.Admin;
import Exceptions.InvalidPhoneNumberException;
import java.util.ArrayList;

public class Main {
    private static final SQLHandler sql = new SQLHandler();
    private static Scanner console = new Scanner(System.in);
    private static Customer customer = new Customer();
    private static Product product = new Product();
    private static Admin admin = new Admin();

    private static void exit(){
        System.out.println("Thank you for visiting our Store.");
        System.gc();
        console.close();
        System.exit(0);
    }

    private static int initialScreenOptions(){
        System.out.println("\n-----------Welcome to our Store----------");
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
        int choice = -1;
        System.out.println("\n-----------Customer Login Page-----------");
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

    private static int adminLoginMenu(){
        console = new Scanner(System.in);

        int choice = -1;
        System.out.println("\n-----------Admin Login Page-----------");
        do {
            System.out.println("1) Admin Login\n2) Previous Screen\n3) Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = console.nextInt();
                if (choice < 1 || choice > 3) {
                    throw new InputMismatchException("Input not Valid");
                }
            }
            catch (InputMismatchException exception) {
                System.out.println("Please enter a valid input!\n");
                if (console.hasNextLine()) {
                    console.nextLine();
                }
            }
        } while(choice < 1 || choice > 3);
        return choice;
    }

    /**
     * This function is used to ask for a valid input according to the Menu Options.
     * @return Valid choice between 1 and 7
     */

    private static int showMenu() {
        System.out.println("\n-------------Menu Page-------------");
        int choice = -10;
        console = new Scanner(System.in);

        do {
            if (choice != -10)
                System.out.println("\nPlease enter a valid choice!");

            System.out.println("1) See Items in the store\n2) Add Items to cart by Product ID\n3) View Items in cart\n4) Edit Items in the cart\n5) Billing\n6) Logout\n7) Exit\n");
            System.out.print("Enter your choice: ");
            if (console.hasNextInt())
                choice = console.nextInt();

            if (console.hasNextLine())
                console.nextLine();


        } while(choice < 1 || choice > 7);
        return choice;
    }

    public static void main (String[] args) {
        while (true) {
            switch (Main.initialScreenOptions()) {

                case 1:
                    int customerLoginChoice;
                    do {
                        customerLoginChoice = Main.customerLoginMenu();
                        switch (customerLoginChoice) {

                            case 2:
                                if (!(sql.customerSignUp()))
                                    break;

                            case 1:
                                String phoneNumber = "";
                                String password = "";
                                Customer login;
                            {
                                do {
                                    try {
                                        if (console.hasNextLine()) {
                                            console.nextLine();
                                        }
                                        System.out.print("Enter your Mobile Number: ");
                                        console = new Scanner(System.in);
                                        if (console.hasNextLine())
                                            phoneNumber = console.nextLine();


                                        if (!(SQLHandler.isValidPhoneNumber(phoneNumber)))
                                            throw new InvalidPhoneNumberException("Not a valid Mobile Number.");
                                    } catch (InvalidPhoneNumberException exception) {
                                        System.out.println("Please enter a valid Phone number!");
                                    } catch (Exception exception) {
                                        System.out.println("Parent Class Exception has occurred in case 1 of customerLoginMenu choice in main().");
                                        System.out.println("Exception Message: " + exception.getMessage());
                                    }

                                    if (!sql.isUserPresent(phoneNumber)) {
                                        System.out.println("Account Not Found");
                                        break;
                                    }

                                } while (!(SQLHandler.isValidPhoneNumber(phoneNumber)));

                                if (!sql.isUserPresent(phoneNumber))
                                    break;

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
                            }

                            if (login == null) {
                                System.out.println("Incorrect Password");

                            } else {
                                customer = new Customer(login);
                                System.out.println("\nLogin Successful.");
                                System.out.println("---Welcome to our Store---");
                                customer.display();

                                int showMenuChoice;
                                ArrayList<Product> cart = new ArrayList<>();
                                do {
                                    showMenuChoice =  Main.showMenu();

                                    switch (showMenuChoice) {
                                        case 1:  //See items in the store
                                            try {
                                                sql.showItems();
                                            } catch (Exception exception) {
                                                System.out.println("Exception Message: " + exception.getMessage());
                                            }
                                            break;

                                        case 2: //Add items to cart by product id;

                                            String cartChoice;
                                            String productId;
                                            int quantity;
                                            do {
                                                System.out.print("Enter the product ID of the item:");
                                                productId = console.nextLine();
                                                productId = productId.trim();

                                                if (!sql.isProductPresent(productId)){
                                                    System.out.println("Product not found. Please check the entered Product ID.");
                                                    break;
                                                }

                                                if (sql.getProduct(productId).getQuantity() == 0){
                                                    System.out.println("Sorry the product items selected are over. Please wait till restock.");
                                                    break;
                                                }

                                                System.out.print("Enter the quantity of the item:");
                                                if (console.hasNextInt()) {
                                                    quantity = console.nextInt();
                                                    if (quantity <= 0) {
                                                        System.out.println("Please enter a positive value.");
                                                        break;
                                                    }
                                                    if (sql.getQuantity(productId) < 0 || sql.getQuantity(productId) < quantity) {
                                                        System.out.println("Quantity exceeds the limit.");
                                                        break;
                                                    }
                                                    sql.sell(productId, quantity);  //Temporarily adds item to cart, will be permanently updated in DataBase after billing
                                                    Product itemToBeAdded = new Product(sql.getProduct(productId));

                                                    if (cart.contains(itemToBeAdded)) {
                                                        int indexOfExistingProduct = cart.indexOf(itemToBeAdded);
                                                        cart.get(cart.indexOf(itemToBeAdded)).setQuantity(quantity + cart.get(indexOfExistingProduct).getQuantity());
                                                    }
                                                    else
                                                    {
                                                        itemToBeAdded.setQuantity(quantity);
                                                        cart.add(new Product(itemToBeAdded));
                                                    }
                                                    System.out.println("Added to cart.\n");
                                                }
                                                else{
                                                    System.out.println("Please enter a valid quantity!");
                                                }

                                                if (console.hasNextLine()) //Clears input memory
                                                    console.nextLine();

                                                System.out.print("Do you want to add more items - Yes to add , any other key to exit: ");
                                                cartChoice = console.nextLine();
                                            }while (cartChoice.matches("(([yY])([eE])([sS]))|y|Y"));
                                            break;

                                        case 3 : //View items in the cart

                                            if (cart.isEmpty()) {
                                                System.out.println("Cart is Empty");
                                                break;
                                            }
                                            else {
                                                System.out.println("Displaying items in the cart -");
                                                System.out.println("+-------+---------------------------+------------+------------+");
                                                System.out.printf("| %-5S | %-25S | %-10S | %-10S |\n","ID","Name","Price","Quantity");
                                                for (Product value : cart) {
                                                    System.out.println(value);
                                                }
                                                System.out.println("+-------+---------------------------+------------+------------+");
                                            }
                                            break;
                                        case 4:
                                            if (cart.isEmpty()) {
                                                System.out.println("Cart is Empty");
                                                break;
                                            }
                                            else {
                                                System.out.println("+-------+---------------------------+------------+------------+");
                                                System.out.printf("| %-5S | %-25S | %-10S | %-10S |\n","ID","Name","Price","Quantity");
                                                for (Product value : cart) {
                                                    System.out.println(value);
                                                }
                                                System.out.println("+-------+---------------------------+------------+------------+");

                                                int editChoice = -1;
                                                do {
                                                    System.out.print("1) Delete all items of product\n2) Edit the quantity\n3) Clear the cart\n4) Previous\nEnter your choice: ");
                                                    if (console.hasNextInt()) {
                                                        editChoice = console.nextInt();
                                                        console.nextLine();
                                                    } else {
                                                        System.out.println("Please enter a valid choice");
                                                        console.nextLine();
                                                    }

                                                }while (editChoice < 1 || editChoice > 3);

                                                switch (editChoice){
                                                    case 1:
                                                        String pId;

                                                        System.out.print("Enter the product ID of the item you want to delete: ");
                                                        pId = console.nextLine().trim();

                                                        for (Product value : cart) {
                                                            if (value.getId().equalsIgnoreCase(pId)) {
                                                                sql.addBackToShelf(pId,value.getQuantity());
                                                                cart.remove(value);
                                                                System.out.println("Deleted product successfully.");
                                                                pId = "nullified";
                                                                break;
                                                            }

                                                        }
                                                        if (!pId.equals("nullified"))
                                                            System.out.println("Product ID is not present in the cart.");

                                                        break;

                                                    case 2:
                                                        System.out.print("Enter the product ID of the item you want to edit: ");
                                                        pId = console.nextLine().trim();
                                                        Product productInCart = null;
                                                        for (Product value: cart){  //Checking if product is present in the cart
                                                            if (value.getId().equalsIgnoreCase(pId)) {
                                                                productInCart = value;
                                                            }
                                                        }
                                                        if (productInCart == null) {
                                                            System.out.println("Entered Product ID is not present in the cart.");
                                                            break;
                                                        }
                                                        int newQuantity = 0;

                                                        Product productInDB = new Product(sql.getProduct(pId)); //The product data in database

                                                        do{
                                                            System.out.print("Enter the new quantity: ");
                                                            if (console.hasNextInt()) {
                                                                newQuantity = console.nextInt();
                                                                console.nextLine();
                                                                System.out.println(newQuantity);
                                                            } else {
                                                                System.out.println("Please enter a numeric value.");
                                                            }
                                                        }while(newQuantity == 0);
                                                        System.out.println("New Quantity:" + newQuantity);

                                                        if (newQuantity > productInDB.getQuantity()+newQuantity) {
                                                            System.out.println("Not enough quantity in stock");
                                                            break;
                                                        }

                                                        productInDB.setQuantity(productInDB.getQuantity() + productInCart.getQuantity() - newQuantity);
                                                        productInCart.setQuantity(newQuantity);
                                                        if (sql.updateItem(productInDB))
                                                            System.out.println("Updated the quantity");
                                                        else
                                                            System.out.println("Failed to update the quantity");

                                                        System.out.println(cart);
                                                        System.out.println(productInCart);
                                                        System.out.println(productInDB);
                                                        break;

                                                    case 3:
                                                        for (Product value : cart) {
                                                            sql.addBackToShelf(value.getId(),value.getQuantity());
                                                        }
                                                        cart.clear();
                                                        System.out.println("Cart Cleared Successfully");
                                                        break;

                                                    case 4:
                                                        break;
                                                }
                                            }
                                            break;
                                        case 6:
                                            System.out.println("Logged Out Successfully.");
                                            break;
                                        case 5:
                                            double totalAmount = 0;
                                            if (cart.isEmpty()) {
                                                System.out.println("No items in the cart to create bill. Please shop.");
                                                break;
                                            }
                                            System.out.println("----------Bill----------");
                                            System.out.println("Name: " + customer.getName() + "\nCustomer ID: " + customer.getId());
                                            System.out.println("+-------+---------------------------+------------+------------+-----------------+");
                                            System.out.printf("| %-5S | %-25S | %-10S | %-10S | %-15S |\n","ID","Name","Price","Quantity","Total");
                                            for (Product value : cart) {
                                                System.out.print(value.toString());
                                                totalAmount += (value.getQuantity() * value.getPrice());
                                                System.out.printf("%-15S |\n",(value.getQuantity() * value.getPrice()));
                                            }
                                            System.out.println("+-------+---------------------------+------------+------------+-----------------+");


                                            customer.addMoneySpent(totalAmount);
                                            sql.updateCustomerData(customer);
                                            sql.billingProcess();
                                            System.out.println("\nPlease pay at the counter!");

                                            System.out.println("Logging out");

                                        case 7: //Exit
                                                Main.exit();
                                    }
                                    if (showMenuChoice == 6 )
                                        break;


                                } while (showMenuChoice != 7);
                            }
                            case 3:
                                break;

                            case 4:
                                Main.exit();
                        }

                    }while(customerLoginChoice != 3);
                    break;
                case 2:

                    int adminLoginChoice = Main.adminLoginMenu();
                    switch (adminLoginChoice) {
                        case 1:
                            String adminId;
                            String password;
                            console = new Scanner(System.in);
                            Admin loginAdmin = null;

                            do{
                                System.out.print("Enter the adminId: ");
                                adminId = console.nextLine().trim();

                                if (!sql.isAdminPresent(adminId)){
                                    System.out.println("Admin Id is not found. Unauthorized Access.");
                                    break;
                                }
                                System.out.print("Enter your password: ");
                                password = console.nextLine().trim();
                                loginAdmin = sql.adminLogin(adminId,password);

                                if (loginAdmin == null)
                                    System.out.println("Incorrect Password. Try again!");

                            }while(loginAdmin == null);

                            if (!sql.isAdminPresent(adminId)){
                                break;
                            }

                            admin = new Admin(loginAdmin);

                            System.out.println("--Welcome--");
                            System.out.println(admin.toString());

                            int adminMenuChoice = -1;

                            do {
                                do {
                                    if (adminMenuChoice != -1 && (adminMenuChoice < 1 || adminMenuChoice > 7))
                                        System.out.println("Please enter a valid choice.");
                                    System.out.println("\n----------Admin Menu----------");
                                    System.out.print("1) View items in the store\n2) Add new Items to the store\n3) Delete items in the store\n4) Update items by product ID\n5) See customer info\n6) Logout\n7) Exit\nEnter your choice: ");
                                    if (console.hasNextInt()) {
                                        adminMenuChoice = console.nextInt();
                                    }
                                    else {
                                        System.out.println("Please enter a valid integer.");
                                        console.nextLine();
                                    }

                                }while (adminMenuChoice < 1 || adminMenuChoice > 7);


                                switch (adminMenuChoice) {
                                    case 1:
                                        sql.showItems();
                                        break;

                                    case 2: //add new items
                                        sql.addNewItem();
                                        break;

                                    case 3: //Delete items

                                        String productId;
                                        console = new Scanner(System.in);

                                        System.out.print("Enter the product Id to delete: ");
                                        productId = console.nextLine().trim();

                                        if (sql.deleteItem(productId)){
                                            System.out.println("Item deleted successfully.");
                                        }
                                        else{
                                            System.out.println("Item not present in the database.");
                                        }
                                        break;

                                    case 4: //Update items  //TODO add update quantity in cart update system

                                        console =  new Scanner(System.in);

                                        System.out.print("Enter the product Id to update: ");
                                        productId = console.nextLine().trim();

                                        if (!sql.isProductPresent(productId)) {
                                            System.out.println("Entered product id is not present in the store.");
                                            break;
                                        }

                                        int newQuantity = -1;
                                        double newPrice = -1;
                                        String newName;

                                        System.out.print("Enter the new name: ");
                                        newName = console.nextLine().trim();

                                        do {
                                            if (newPrice != -1)
                                                System.out.println("Please enter a positive value");
                                            System.out.print("Enter the new price: ");
                                            if (console.hasNextDouble()) {
                                                newPrice = console.nextDouble();
                                                console.nextLine();
                                            }
                                            else {
                                                System.out.print("Enter a numeric value.");
                                                console.nextLine();
                                                break;
                                            }

                                        }while (newPrice < 0);

                                        if (newPrice == -1)
                                            break;

                                        do {
                                            if (newQuantity != -1)
                                                System.out.println("Please enter a positive value");

                                            System.out.print("Enter the new quantity: ");
                                            if (console.hasNextInt()) {
                                                newQuantity = console.nextInt();
                                            }
                                            else {
                                                System.out.print("Enter a numeric value.");
                                                console.nextLine();
                                                break;
                                            }

                                        }while (newQuantity < 0);

                                        if (newQuantity == -1)
                                            break;

                                        if (sql.updateItem(new Product(productId,newName,newPrice,newQuantity)))
                                            System.out.println("Product updated successfully.");
                                        else
                                            System.out.println("Some error occurred in updating the product");

                                        break;

                                    case 5: //See customer info
                                        sql.showCustomersData(admin);
                                        break;
                                    case 6: //Logout
                                        System.out.println("Logging Out");
                                        break;

                                    case 7: //Exit
                                        Main.exit();
                                }

                            }while(adminMenuChoice != 6);
                            break;

                        case 2:
                            break;

                        case 3:
                            Main.exit();
                    }


                    break;
                case 3:
                    Main.exit();
            }
        }
    }
}