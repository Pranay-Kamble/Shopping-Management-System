package Classes;

import SQL.SQLHandler;

public final class Product {
    private static int ID;
    static {
        ID = SQLHandler.initialProducts + 1;
    }

    private String name;
    private double price;
    private int quantity;
    private String productId = "P";

    public String getId() {
        return this.productId;
    }
    public String getName() {return this.name;}
    public double getPrice() {return this.price;}
    public int getQuantity() {return this.quantity;}


    public Product() { }

    public Product(Product copy) {
        this.productId = copy.productId;
        this.name = copy.name;
        this.price = copy.price;
        this.quantity = copy.quantity;
    }

    public Product(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productId = this.productId + (Product.ID++);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String productId, String name, double price, int quantity){
        this(name,price,quantity);
        this.productId = productId;
    }

    public String toString(){
        return String.format("| %-5s | %-25s | %-10.2f | %-10s | ", this.productId, this.name,this.price, this.quantity);

    }

    public boolean equals(Object other){
        if (other instanceof Product){
            return this.productId.equalsIgnoreCase(((Product)other).productId);
        }
        return false;
    }

}