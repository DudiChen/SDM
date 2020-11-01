package servlet.pojo;

import controller.Controller;
import entity.OrderInvoice;
import servlet.util.ServletUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreOrderDTO {
    String customerName;
    String id;
    String date;
    int totalProducts;
    double totalProductsPrice;
    double deliveryPrice;

    public StoreOrderDTO(OrderInvoice orderInvoice) {
        this.customerName = Controller.getInstance().getCustomerById(orderInvoice.getCustomerId()).getName();
        this.id = Integer.toString(orderInvoice.getOrderId());
        this.date = ServletUtils.formatDateToString(orderInvoice.getDeliveryDate());
        this.totalProducts = orderInvoice.getNumberOfInvoiceProducts();
        this.totalProductsPrice = orderInvoice.getTotalProductsPrice();
        this.deliveryPrice = orderInvoice.getShipmentPrice();
    }
}
