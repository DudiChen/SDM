package servlet.pojo;

public class ProductInOrderDTO {
    String id;
    String name;
    String purchaseMethod;
    double quantity;
    double price;
    double total;

    public ProductInOrderDTO(String id, String name, String purchaseMethod, double quantity, double price, double total) {
        this.id = id;
        this.name = name;
        this.purchaseMethod = purchaseMethod;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }
}
