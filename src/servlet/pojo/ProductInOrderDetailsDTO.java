package servlet.pojo;

public class ProductInOrderDetailsDTO {
    String id;
    String name;
    String purchaseMethod;
    double purchasedQuantity;
    double price;
    double totalPrice;
    boolean isPartOfDiscount;

    public ProductInOrderDetailsDTO(String id, String name, String purchaseMethod, double purchasedQuantity, double price, double totalPrice, boolean isPartOfDiscount) {
        this.id = id;
        this.name = name;
        this.purchaseMethod = purchaseMethod;
        this.purchasedQuantity = purchasedQuantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.isPartOfDiscount = isPartOfDiscount;
    }
}
