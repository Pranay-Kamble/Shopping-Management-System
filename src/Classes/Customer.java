package Classes;


import SQL.SQLHandler;

public final class Customer extends Person {
    private static int ID;
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

    public Customer(Customer customer) {
        this(customer.getId(),customer.getName(),customer.getAge(),customer.getPhoneNumber(),customer.getPassword(),customer.getMoneySpent());
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

    public void addMoneySpent(double spentAmount){
        this.moneySpent += spentAmount;
    }

    public boolean equals(Customer c){
        return this.name.equals(c.name) && this.age == c.age && this.password.equals(c.password) && this.phoneNumber.equals(c.phoneNumber);
    }

    public String toString(){
        return "\nUser Name: " + this.name +  "\nUser ID: " + this.custId + "\nAge: " + this.age + "\nPhone Number: " + this.phoneNumber + "\nMoneySpent: " + this.moneySpent ;
    }

    public String toLine() {
        return String.format("| %-5S | %-25S | %-4d | %-12S | %-11.3f |", this.custId, this.name, this.age, this.phoneNumber ,this.moneySpent);
    }
}