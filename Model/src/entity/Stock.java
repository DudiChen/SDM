package entity;

import java.util.Map;

public class Stock {

    private Map<Integer, StoreProduct> soldProducts;

    public Stock(Map<Integer, StoreProduct> soldProducts) {
        this.soldProducts = soldProducts;
    }

    public boolean doesProductIdExist(int productId) {
        return this.soldProducts.containsKey(productId);
    }

    public Map<Integer, StoreProduct> getSoldProducts() {
        return soldProducts;
    }

    public double getProductPrice(int productId) {
        return soldProducts.get(productId).getPrice();
    }

    public void delete(int productId) {
        this.soldProducts.remove(productId);
    }

    public void addSoldProduct(StoreProduct storeProduct) {
        this.soldProducts.put(storeProduct.getId(), storeProduct);
    }

    public StoreProduct getSoldProductById(int storeId) {
        return this.soldProducts.get(storeId);
    }
}
