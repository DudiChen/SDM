package servlet.pojo;

import entity.InvoiceDiscountProduct;
import entity.InvoiceProduct;
import entity.StoreProduct;
import servlet.util.ServletUtils;

public class ProductInOrderDetailsDTO {
    String id;
    String name;
    String purchaseMethod;
    double purchasedQuantity;
    double price;
    double totalPrice;
    boolean isPartOfDiscount;

    public ProductInOrderDetailsDTO(InvoiceProduct invoiceProduct) {

        this.id = Integer.toString(invoiceProduct.getId());
        this.name = invoiceProduct.getName();
        this.purchaseMethod = invoiceProduct.getPurchaseMethod();
        this.purchasedQuantity = invoiceProduct.getQuantity();
        this.price = invoiceProduct.getPrice();
        this.totalPrice = invoiceProduct.getTotalPrice();
        this.isPartOfDiscount = invoiceProduct.isPartOfDiscount();
    }
}
