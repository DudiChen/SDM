package builder;

import entity.Customer;
import entity.Area;
import entity.Product;
import entity.Store;
import entity.market.MarketUtils;
import jaxb.generated.*;
import util.ErrorMessage;

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import builder.validate.DataValidationUtils;

import static builder.validate.DataValidationUtils.preliminaryDataValidation;

public class AreaBuilder implements Builder<SuperDuperMarketDescriptor, Area> {
    Map<Integer, Product> idToProduct;
    Map<Integer, Store> idToStore;
    int areaId;
    String name;
    Customer owner;

    public AreaBuilder(int newAreaId, Customer owner) {
        this.areaId = newAreaId;
        this.owner = owner;
    }

    // TODO: add check for existing area by name
    @Override
    public Area build(SuperDuperMarketDescriptor source) throws ValidationException {
        preliminaryDataValidation(source); // Performs all preliminary data validation checking invalid values
        idToProduct = getIdToProduct(source.getSDMItems().getSDMItem());
        DataValidationUtils.postProductsStoreDataValidation(idToProduct, source.getSDMStores().getSDMStore()); // Performs Products related checks in Stores
        idToStore = getIdToStore(source.getSDMStores().getSDMStore());
        name = source.getSDMZone().getName();
        return new Area(areaId, name, owner, idToStore, idToProduct);
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
        return sdmStores.stream().collect(Collectors.toMap(SDMStore::getId, store -> new StoreBuilder(idToProduct, areaId, owner.getName()).build(store)));
    }
}
