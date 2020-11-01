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

    public ProductInAreaDTO(Product product, int areaId) {
        this.name = product.getName();
        this.id = Integer.toString(product.getId());
        this.purchaseMethod = product.getPurchaseMethod().getName();
        this.sellingStores = Controller.getInstance().getNumberOfStoresThatSellProduct(areaId, product.getId());
        this.averagePrice = Controller.getInstance().getAverageProductPrice(areaId, product.getId());
        this.sells = Controller.getInstance().getProductNumberOfSales(areaId, product.getId());
    }
}
