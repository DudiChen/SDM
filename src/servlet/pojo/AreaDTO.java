package servlet.pojo;

public class AreaDTO {
    String name;
    String owner;
    int totalProducts;
    int totalStores;
    int totalOrders;
    double avgOrderCost;

    public AreaDTO(String name, String owner, int totalProducts, int totalStores, int totalOrders, double avgOrderCost) {
        this.name = name;
        this.owner = owner;
        this.totalProducts = totalProducts;
        this.totalStores = totalStores;
        this.totalOrders = totalOrders;
        this.avgOrderCost = avgOrderCost;
    }
}
