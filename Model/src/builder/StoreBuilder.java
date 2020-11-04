package builder;

import entity.Discount;
import entity.Product;
import entity.Store;
import jaxb.generated.SDMStore;

//import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoreBuilder implements Builder<SDMStore, Store> {

    Map<Integer, Product> idToProduct;
    int areaId;
    String ownerName;

    public StoreBuilder(Map<Integer, Product> idToProduct, int areaId, String ownerName) {
        this.idToProduct = idToProduct;
        this.areaId = areaId;
        this.ownerName = ownerName;
    }

    @Override
    public Store build(SDMStore source) {
        return new Store(
                new PointBuilder().build(source.getLocation()),
                new StockBuilder(this.idToProduct).build(source.getSDMPrices()),
                source.getDeliveryPpk(),
                source.getId(),
                source.getName(),
                this.areaId,
                this.ownerName,
                getDiscountMap(source)
        );
    }

    private Map<Integer, List<Discount>> getDiscountMap(SDMStore source) {
        Map<Integer, List<Discount>> result = new HashMap<>();
        if  (source.getSDMDiscounts() != null && !source.getSDMDiscounts().getSDMDiscount().isEmpty()) {
            result = source.getSDMDiscounts().getSDMDiscount()
                    .stream()
                    .map(sdmDiscount -> new DiscountBuilder().build(sdmDiscount))
                    .collect(Collectors.groupingBy(Discount::getProductId,Collectors.toList()));
//                    .collect(Collectors.toMap(Discount::getProductId,discount -> discount));
        }

        return result;
    }
}
