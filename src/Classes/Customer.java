package Classes;

public final class Customer extends Person {
    private static int ID = 1;
    private String phoneNumber;
    private String password;
    private String custId = "C-";
    private double moneySpent;

    public Customer() { } //Default Constructor

    public Customer(String name, int age, String phoneNumber, String password) { //Parameterised Constructor
        super(name,age);
        this.custId = this.custId + (Customer.ID++);
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Customer(String custId, String name, int age, String phoneNumber,String password, double moneySpent){
        this(name,age,phoneNumber,password);
        this.custId = custId;
        this.moneySpent = moneySpent;
    }

    public String getPassword() {
        return this.password;
    }
    public String getCustId() {
        return this.custId;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public double getMoneySpent(){
        return this.moneySpent;
    }

    public void display() {
        System.out.println("Name: " + this.name +  "\nID: " + this.custId + "\nAge: " + this.age + "\nPhone Number: " + this.phoneNumber + "\nPassword: " + this.password);
    }

    public boolean isValid(){
        if (this.phoneNumber == null ||this.phoneNumber.isEmpty())
            return false;

        return true;
    }

}