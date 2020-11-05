package servlet.pojo;

import controller.Controller;
import entity.InvoiceDiscountProduct;
import entity.InvoiceProduct;
import entity.Product;
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
        this.isPartOfDiscount = false;
    }

    public ProductInOrderDetailsDTO(InvoiceDiscountProduct invoiceDiscountProduct) {
        this.id = Integer.toString(invoiceDiscountProduct.getProductId());
        this.name = invoiceDiscountProduct.getName();
        this.purchaseMethod = invoiceDiscountProduct.getPurchaseMethod().toString();
        this.purchasedQuantity = invoiceDiscountProduct.getQuantity();
        this.price = invoiceDiscountProduct.getAdditionalCost();
        this.totalPrice = this.price;
        this.isPartOfDiscount = true;
    }
}
