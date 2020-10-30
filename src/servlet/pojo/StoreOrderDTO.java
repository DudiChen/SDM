package servlet.pojo;

public class StoreOrderDTO {
    String customerName;
    String id;
    String date;
    int totalProducts;
    double totalProductsPrice;
    double deliveryPrice;

    public StoreOrderDTO(String customerName, String id, String date, int totalProducts, double totalProductsPrice, double deliveryPrice) {
        this.customerName = customerName;
        this.id = id;
        this.date = date;
        this.totalProducts = totalProducts;
        this.totalProductsPrice = totalProductsPrice;
        this.deliveryPrice = deliveryPrice;
    }
}
