package Classes;


import SQL.SQLHandler;

public final class Customer extends Person {
    public static int ID;
    private String phoneNumber;
    private String password;
    private String custId = "C";
    private double moneySpent;


    public Customer() {
        ID = SQLHandler.initialCustomers + 1;
        System.out.println("Cust ID: " + Customer.ID);
    } //Default Constructor

    public Customer(String name, int age, String phoneNumber, String password) { //Parameterised Constructor
        super(name,age);
        this.custId = "C" + (Customer.ID++);
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Customer(String custId, String name, int age, String phoneNumber,String password, double moneySpent){
        super(name,age);
        this.phoneNumber=phoneNumber;
        this.password=password;
        this.custId = custId;
        this.moneySpent = moneySpent;
    }

    public String getPassword() {
        return this.password;
    }
    public int getAge() {
        return this.age;
    }
    public String getId() {
        return this.custId;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public double getMoneySpent(){
        return this.moneySpent;
    }
    public String getName() { return this.name; }


    public void display() {
        System.out.println("\nUser Name: " + this.name +  "\nUser ID: " + this.custId + "\nAge: " + this.age + "\nPhone Number: " + this.phoneNumber);
    }

    public boolean isValid(){
        if (this.phoneNumber == null ||this.phoneNumber.isEmpty())
            return false;

        return true;
    }

    public void addMoneySpent(double amount){
        this.moneySpent += amount;
    }

    public boolean equals(Customer c){
        return this.name.equals(c.name) && this.age == c.age && this.password.equals(c.password) && this.phoneNumber.equals(c.phoneNumber);
    }
}