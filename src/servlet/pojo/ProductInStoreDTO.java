package servlet.pojo;

import entity.StoreProduct;

public class ProductInStoreDTO {
     String name;
     String id;
     String purchaseMethod;
     double price;

    public ProductInStoreDTO(StoreProduct product) {
        this.name = product.getName();
        this.id = Integer.toString(product.getId());
        this.purchaseMethod = product.getProduct().getPurchaseMethod().getName();
        this.price = product.getPrice();
    }
}
