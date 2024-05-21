package Exceptions;

public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String msg) {
        super(msg);
    }

    public InvalidPhoneNumberException(){
        System.out.println("Please enter a valid 10 digit Phone Number");
    }
}
