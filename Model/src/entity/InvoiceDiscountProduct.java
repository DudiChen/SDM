package entity;

import entity.Discount;

import java.io.Serializable;

public class InvoiceDiscountProduct implements Serializable {
    private int productId;
    private String name;
    private double additionalCost;
    private double quantity;
    private String discountName;
    Product.PurchaseMethod purchaseMethod;

    public InvoiceDiscountProduct(int productId,
                                  String name,
                                  double additionalCost,
                                  double quantity,
                                  Product.PurchaseMethod purchaseMethod,
                                  String discountName) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
        this.discountName = discountName;
        this.purchaseMethod = purchaseMethod;
    }

    public InvoiceDiscountProduct(Discount.Offer acceptedOffer, String productName, Product.PurchaseMethod purchaseMethod) {
        this.productId = acceptedOffer.getProductId();
        this.name = productName;
        this.quantity = acceptedOffer.getQuantity();
        this.additionalCost = acceptedOffer.getForAdditional();
        this.discountName = acceptedOffer.getRelatedDiscountName();
        this.purchaseMethod = purchaseMethod;
    }

    public String getName() {
        return name;
    }

    public double getAdditionalCost() {
        return this.additionalCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public Product.PurchaseMethod getPurchaseMethod() {
        return this.purchaseMethod;
    }
}
