package servlet.pojo;

public class ProductInAreaDTO {
    String name;
    String id;
    String purchaseMethod;
    int sellingStores;
    double averagePrice;
    int sells;

    public ProductInAreaDTO(String name, String id, String purchaseMethod, int sellingStores, double averagePrice, int sells) {
        this.name = name;
        this.id = id;
        this.purchaseMethod = purchaseMethod;
        this.sellingStores = sellingStores;
        this.averagePrice = averagePrice;
        this.sells = sells;
    }
}
