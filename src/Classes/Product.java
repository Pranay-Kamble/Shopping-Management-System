package Classes;

public final class Product {
    private static int ID = 1;

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

    public Product(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productId = this.productId + (Product.ID++);
    }

    public Product(String productId, String name, double price, int quantity){
        this(name,price,quantity);
        this.productId = productId;
    }

    public String toString(){
        return String.format("%-6s - %-15s - %-8.3f - %-5d", this.productId, this.name,this.price, this.quantity);

    }

}