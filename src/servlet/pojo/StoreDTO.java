package servlet.pojo;

import entity.Store;

public class StoreDTO {
    String name;
    String id;
    String owner;
    int totalOrders;
    double productsIncome;
    double shippingsIncome;
    int ppk;

    public StoreDTO(Store store) {
        this.name = store.getName();
        this.id = Integer.toString(store.getId());
        this.owner = store.getOwnerName();
        this.totalOrders = store.getOrdersHistory().size();
        this.productsIncome = store.getTotalProductsIncome();
        this.shippingsIncome = store.getTotalShipmentIncome();
        this.ppk = store.getPpk();
    }
}
