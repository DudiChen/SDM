package servlet.pojo;

public class StoreDTO {
    String name;
    String id;
    String owner;
    int totalOrders;
    double productsIncome;
    double shippingsIncome;
    int ppk;

    public StoreDTO(String name, String id, String owner, int totalOrders, double productsIncome, double shippingsIncome, int ppk) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.totalOrders = totalOrders;
        this.productsIncome = productsIncome;
        this.shippingsIncome = shippingsIncome;
        this.ppk = ppk;
    }
}
