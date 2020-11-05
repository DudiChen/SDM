package servlet.pojo;

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

    // TODO: See if this is ok - consider making DTO for InvoiceDiscountProduct
    public ProductInOrderDetailsDTO(InvoiceDiscountProduct invoiceDiscountProduct) {
        this.id = Integer.toString(invoiceDiscountProduct.getProductId());
        this.name = invoiceDiscountProduct.getName();
        this.purchaseMethod = "Irrelevant";
        this.purchasedQuantity = invoiceDiscountProduct.getQuantity();
        this.price = invoiceDiscountProduct.getAdditionalCost();
        this.totalPrice = this.price;
        this.isPartOfDiscount = true;
    }
}
