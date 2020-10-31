package servlet.pojo;

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

    public StoreOrderDTO(String customerName, String id, Date date, int totalProducts, double totalProductsPrice, double deliveryPrice) {
        this.customerName = customerName;
        this.id = id;
        this.date = ServletUtils.formatDateToString(date);
        this.totalProducts = totalProducts;
        this.totalProductsPrice = totalProductsPrice;
        this.deliveryPrice = deliveryPrice;
    }
}
