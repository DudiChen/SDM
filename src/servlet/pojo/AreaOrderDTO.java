package servlet.pojo;

import controller.Controller;
import entity.Area;
import entity.OrderInvoice;
import entity.Store;
import servlet.util.ServletUtils;

public class AreaOrderDTO {

    String id;
    int totalProducts;
    double deliveryPrice;
    String storeName;

    public AreaOrderDTO(OrderInvoice orderInvoice, Area area) {
        this.id = Integer.toString(orderInvoice.getOrderId());
        this.totalProducts = orderInvoice.getNumberOfInvoiceProducts();
        this.deliveryPrice = orderInvoice.getShipmentPrice();
        this.storeName = area.getStoreById(orderInvoice.getStoreId()).getName();
    }
}
