package servlet.pojo;

public class ProductInStoreDTO {
     String name;
     String id;
     String purchaseMethod;
     double price;

    public ProductInStoreDTO(String name, String id, String purchaseMethod, double price) {
        this.name = name;
        this.id = id;
        this.purchaseMethod = purchaseMethod;
        this.price = price;
    }
}
