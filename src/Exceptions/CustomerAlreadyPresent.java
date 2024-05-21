package Exceptions;

public class CustomerAlreadyPresent extends Exception {
    public CustomerAlreadyPresent(String message){
        super(message);
    }

    public CustomerAlreadyPresent (){
        System.out.println("Customer Phone Number is already present");
    }
}