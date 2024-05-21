package Classes;

public final class Product {
    private static int ID = 1;

    private String name;
    private double price;
    private int quantity;
    private String productId = "P-";

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
}