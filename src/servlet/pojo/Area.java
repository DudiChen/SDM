package servlet.pojo;

public class Area {
    String name;
    String owner;
    int totalProducts;
    int totalStores;
    int totalOrders;
    int avgOrderCost;

    public Area(String name, String owner, int totalProducts, int totalStores, int totalOrders, int avgOrderCost) {
        this.name = name;
        this.owner = owner;
        this.totalProducts = totalProducts;
        this.totalStores = totalStores;
        this.totalOrders = totalOrders;
        this.avgOrderCost = avgOrderCost;
    }
}
