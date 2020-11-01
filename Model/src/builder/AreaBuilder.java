package builder;

import entity.Customer;
import entity.Area;
import entity.Product;
import entity.Store;
import jaxb.generated.*;
import util.ErrorMessage;

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import builder.validate.DataValidationUtils;

public class AreaBuilder implements Builder<SuperDuperMarketDescriptor, Area> {
    Map<Integer, Product> idToProduct;
    Map<Integer, Store> idToStore;
    Map<Integer, Customer> idToCustomer;

    @Override
    public Area build(SuperDuperMarketDescriptor source) throws ValidationException {
        idToProduct = getIdToProduct(source.getSDMItems().getSDMItem());
        DataValidationUtils.postProductsStoreDataValidation(idToProduct, source.getSDMStores().getSDMStore()); // Performs Products related checks in Stores
        idToStore = getIdToStore(source.getSDMStores().getSDMStore());

        return new Area(idToStore, idToProduct);
    }

    private Map<Integer, Product> getIdToProduct(List<SDMItem> sdmItems) {
        return constructIdToProduct(new HashSet<>(sdmItems));
    }

    private Map<Integer, Store> getIdToStore(List<SDMStore> sdmStores) {
        return constructIdToStore(new HashSet<>(sdmStores));
    }

    private Map<Integer, Product> constructIdToProduct(Set<SDMItem> sdmItems) {
        return sdmItems.stream().collect(Collectors.toMap(SDMItem::getId, item -> new ProductBuilder().build(item)));
    }

    private Map<Integer, Store> constructIdToStore(Set<SDMStore> sdmStores) {
        return sdmStores.stream().collect(Collectors.toMap(SDMStore::getId, store -> new StoreBuilder(idToProduct).build(store)));
    }
}
