package servlet.pojo;

import controller.Controller;
import entity.Area;
import exception.MarketIsEmptyException;

public class AreaDTO {
    String id;
    String name;
    String owner;
    int totalProducts;
    int totalStores;
    int totalOrders;
    double avgOrderCost;

    public AreaDTO(Area area) {//throws MarketIsEmptyException {
        this.id = Integer.toString(area.getId());
        this.name = area.getName();
        this.owner = area.getOwnerName();
        this.totalProducts = area.getAllProducts().size();
        this.totalStores = area.getAllStores().size();
        this.totalOrders = area.getOrdersHistory().size();
        this.avgOrderCost = Controller.getInstance().getAreaAverageOrderCost(area.getId());
    }
}
