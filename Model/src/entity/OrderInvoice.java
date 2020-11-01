package entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class OrderInvoice implements Serializable {

    private OrderStatus orderStatus = OrderStatus.ISSUED;
    private int orderId;
    private int customerId;
    private List<InvoiceProduct> invoiceProducts;
    private List<InvoiceDiscountProduct> discountProducts;
//    TODO: possible inconsistency with price type across classes
    private double totalPrice;
    private Date deliveryDate;
    private int storeId;
    private double shipmentPrice;

    public int getCustomerId() { return this.customerId; }

    public double getShipmentPrice() {
        return shipmentPrice;
    }

    public OrderInvoice(int orderId, int customerId, List<InvoiceProduct> invoiceProducts, List<InvoiceDiscountProduct> discountProducts, double totalPrice, Date deliveryDate, int storeId, double shipmentPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.invoiceProducts = invoiceProducts;
        this.discountProducts = discountProducts;
        this.totalPrice = totalPrice;
        this.deliveryDate = deliveryDate;
        this.storeId = storeId;
        this.shipmentPrice = shipmentPrice;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public int getNumberOfInvoiceProducts() { return invoiceProducts.size(); }

    public List<InvoiceDiscountProduct> getDiscountProducts() {
        return discountProducts;
    }

    public int getNumberOfDiscountProducts() { return discountProducts.size(); }

    public void setDiscountProducts(List<InvoiceDiscountProduct> value) {
        this.discountProducts = value;
        for (InvoiceDiscountProduct discountProduct : this.discountProducts) {
            this.totalPrice += discountProduct.getAdditionalCost();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalProductsPrice() {
        return invoiceProducts.stream().map(InvoiceProduct::getPrice).reduce(0.0, Double::sum);
    }

    public int getStoreId() {
        return storeId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    enum OrderStatus implements Serializable {
        ACCEPTED, CANCELED, ISSUED
    }
}
