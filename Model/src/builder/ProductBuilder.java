package builder;

import entity.Product;

public class ProductBuilder implements Builder<SDMItem, Product> {
    @Override
    public Product build(SDMItem source) {
        return new Product(
          source.getId(),
          source.getName(),
          Product.PurchaseMethod.valueOf(source.getPurchaseCategory().toUpperCase())
        );
    }
}
