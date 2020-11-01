package servlet.pojo;

import controller.Controller;
import entity.Product;

public class ProductInAreaDTO {
    String name;
    String id;
    String purchaseMethod;
    int sellingStores;
    double averagePrice;
    int sells;

    public ProductInAreaDTO(Product product) {
        this.name = product.getName();
        this.id = product.getId();
        this.purchaseMethod = product.getPurchaseMethod();
        this.sellingStores = Controller.getInstance().getNumberStoresThatSellProduct(product.getId());
        this.averagePrice = Controller.getInstance().getAverageProductPrice();
        this.sells = Controller.getProductNumberOfSales(product.getId());
    }
}
