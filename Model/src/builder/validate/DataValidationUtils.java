package builder.validate;

import builder.PointBuilder;
import entity.Product;
import jaxb.generated.*;
import util.ErrorMessage;

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DataValidationUtils {
    public static void preliminaryDataValidation(SuperDuperMarketDescriptor source) throws ValidationException {
        ErrorMessage errorMessage = new ErrorMessage("");
        List<SDMStore> sdmStores = source.getSDMStores().getSDMStore();
        List<SDMItem> sdmItems = source.getSDMItems().getSDMItem();
//        List<SDMCustomer> sdmCustomers = source.getSDMCustomers().getSDMCustomer();

        // Checking for invalid Store coordinates:
        boolean foundInvalidStoreCoordinates = checkInvalidCoordinatesInStores(errorMessage, new HashSet<>(sdmStores));

        // Checking for invalid Customer coordinates:
//        boolean foundInvalidCustomerCoordinates = checkInvalidCoordinatesInCustomers(errorMessage, new HashSet<>(sdmCustomers));

        // Checking for Stores and Customers location duplicates:
        List<Point> locations = sdmStores.stream()
                .map(sdmStore -> new PointBuilder().build(sdmStore.getLocation()))
                .collect(Collectors.toList());
//        locations.addAll(sdmCustomers.stream()
//                .map(sdmCustomer -> new PointBuilder().build(sdmCustomer.getLocation()))
//                .collect(Collectors.toList()));
        boolean foundLocationDuplicates = checkLocationDuplicates(errorMessage, locations);

        // Checking for Customer ID duplicates:
//        List<Integer> customerIds = sdmCustomers.stream()
//                .map(SDMCustomer::getId)
//                .collect(Collectors.toList());
//        boolean foundCustomerIdDuplicates = handleDuplicateIds(errorMessage, customerIds, "Customer ID");

        // Checking for Store ID duplicates:
        List<Integer> storeIds = sdmStores.stream()
                .map(SDMStore::getId)
                .collect(Collectors.toList());
        boolean foundStoreIdDuplicates = handleDuplicateIds(errorMessage, storeIds, "Store ID");

        // Checking for Product ID duplicates in all available products:
        List<Integer> productIds = sdmItems.stream()
                .map(SDMItem::getId)
                .collect(Collectors.toList());
        boolean foundProductIdDuplicates = handleDuplicateIds(errorMessage, productIds, "Product ID");

        // Check for Products ID duplicates in each Store:
        boolean foundStoreProductsDuplicates = checkStoreProductDuplicates(errorMessage, sdmStores);

        // Check for Store Non-Existent Product Ids in Discounts:
        boolean foundNonExistingDiscountsProductIdsInStores = checkNonExistingDiscountsProductIdsInStores(errorMessage, sdmStores);

        // Check invalid conditions and throw exception accordingly:
        if (foundInvalidStoreCoordinates || foundLocationDuplicates
                || foundStoreIdDuplicates || foundProductIdDuplicates || foundStoreProductsDuplicates || foundNonExistingDiscountsProductIdsInStores) {
            throw new ValidationException(errorMessage.getMessage());
        }
    }

    private static boolean checkInvalidCoordinatesInStores(ErrorMessage errorMessage, Set<SDMStore> stores) {
        boolean foundBadCoordinates = false;
        Set<Integer> invalidStoreCoordinates =  stores.stream()
                .filter(store -> store.getLocation().getX() < 0 || store.getLocation().getY() < 0 || store.getLocation().getX() > 50 || store.getLocation().getY() > 50)
                .map(SDMStore::getId)
                .collect(Collectors.toSet());

        if (invalidStoreCoordinates.size() > 0) {
            invalidStoreCoordinates
                    .forEach(storeId -> errorMessage.appendMessage("store id " + storeId + " has illegal location - coordinates must be in the range of [0, 50]" + System.lineSeparator()));
            foundBadCoordinates = true;
        }
        return foundBadCoordinates;
    }

    private static boolean checkLocationDuplicates(ErrorMessage dupPointsErrors, List<Point> locations) {
        boolean foundDuplicates = false;
        Set<Point> dupPoints = findDuplicates(locations);
        if (dupPoints.size() > 0) {
            dupPointsErrors.appendMessage(dupPoints.stream().map(point -> "(" + (int)point.getX() + "," + (int)point.getY() + ")")
                    .reduce("", (acc, current) -> acc + "duplicate location coordinate for Point: " + current + System.lineSeparator()));
            foundDuplicates = true;
        }
        return foundDuplicates;
    }

    private static boolean handleDuplicateIds(ErrorMessage dupIdErrors, List<Integer> ids, String entityName) {
        boolean foundDuplicates = false;
        Set<Integer> dupIds = findDuplicates(ids);
        if (dupIds.size() > 0) {
            dupIdErrors.appendMessage(dupIds.stream().map(Object::toString)
                    .reduce("", (acc, current) -> acc + "duplicate id for " + entityName + " " + current + System.lineSeparator()));
            foundDuplicates = true;
        }
        return foundDuplicates;
    }

    private static boolean checkStoreProductDuplicates(ErrorMessage errorMessage, List<SDMStore> sdmStores) {
        boolean foundDuplicateStoreProducts = false;
        Map<Integer, List<Integer>> nonValidStoreIdsToDuplicateProds = getDuplicateProducts(sdmStores);
        nonValidStoreIdsToDuplicateProds
                .forEach((storeId, productIds) -> {
                    if (productIds.size() > 0) {
                        productIds
                                .forEach(productId -> errorMessage.appendMessage("product id " + productId + " sold by store id " + storeId + " more then once" + System.lineSeparator()));
                    }
                });

        if (!nonValidStoreIdsToDuplicateProds.isEmpty()) {
            foundDuplicateStoreProducts = true;
        }

        return foundDuplicateStoreProducts;
    }

    private static boolean checkNonExistingDiscountsProductIdsInStores(ErrorMessage errorMessage, List<SDMStore> sdmStores) {
        int itemId;
        Map<Integer, Map<String,List<Integer>>> storeIdToMapDiscountNameToNonExistItemIds = new HashMap<>();
        for (SDMStore sdmStore : sdmStores) {
            List<Integer> storeProductIds = sdmStore.getSDMPrices().getSDMSell().stream().map(SDMSell::getItemId).collect(Collectors.toList());
            if (sdmStore.getSDMDiscounts() != null && !sdmStore.getSDMDiscounts().getSDMDiscount().isEmpty()) {
                for (SDMDiscount sdmDiscount : sdmStore.getSDMDiscounts().getSDMDiscount()) {
                    itemId = sdmDiscount.getIfYouBuy().getItemId();
                    if (!storeProductIds.contains(itemId)) {
                        nonExistingDiscountsProductIdsInStoresMapHandler(
                                storeIdToMapDiscountNameToNonExistItemIds,
                                sdmStore.getId(),
                                sdmDiscount.getName(),
                                itemId);
                    }
                    for (SDMOffer sdmOffer : sdmDiscount.getThenYouGet().getSDMOffer()) {
                        itemId = sdmOffer.getItemId();
                        if (!storeProductIds.contains(itemId)) {
                            nonExistingDiscountsProductIdsInStoresMapHandler(
                                    storeIdToMapDiscountNameToNonExistItemIds,
                                    sdmStore.getId(),
                                    sdmDiscount.getName(),
                                    itemId);
                        }
                    }
                }
            }
        }

        boolean foundINonExistingProductsIds = !storeIdToMapDiscountNameToNonExistItemIds.isEmpty();
        if (foundINonExistingProductsIds) {
            StringBuilder lineBuilder = new StringBuilder();
            for (Map.Entry<Integer, Map<String,List<Integer>>> storeEntry : storeIdToMapDiscountNameToNonExistItemIds.entrySet()) {
                for (Map.Entry<String, List<Integer>> discountEntry : storeEntry.getValue().entrySet()) {
                    lineBuilder.append("Store Id ").append(storeEntry.getKey()).append(" has Discount ")
                            .append("\"").append(discountEntry.getKey()).append("\"").append(" with the following Item Ids not available in store: ");
                    for (int i = 0;  i < discountEntry.getValue().size(); i++) {
                        if (i > 0) lineBuilder.append(", ");
                        lineBuilder.append(discountEntry.getValue().get(i));
                    }
                }
                lineBuilder.append(System.lineSeparator());
                errorMessage.appendMessage(lineBuilder.toString());
                lineBuilder = new StringBuilder();
            }
        }

        return foundINonExistingProductsIds;
    }

    private static void nonExistingDiscountsProductIdsInStoresMapHandler(Map<Integer, Map<String,List<Integer>>> storeIdToMapDiscountNameToNonExistItemIds, int storeId, String discountName, int itemId) {
        storeIdToMapDiscountNameToNonExistItemIds.computeIfAbsent(storeId, k -> new HashMap<>());
        storeIdToMapDiscountNameToNonExistItemIds.get(storeId).computeIfAbsent(discountName, k -> new ArrayList<>());
        storeIdToMapDiscountNameToNonExistItemIds.get(storeId).get(discountName).add(itemId);
    }

//    public boolean checkInvalidCoordinatesInCustomers(ErrorMessage errorMessage, Set<SDMCustomer> customers) {
//        boolean foundBadCoordinates = false;
//        Set<Integer> invalidCustomerCoordinates =  customers.stream()
//                .filter(store -> store.getLocation().getX() < 0 || store.getLocation().getY() < 0 || store.getLocation().getX() > 50 || store.getLocation().getY() > 50)
//                .map(SDMCustomer::getId)
//                .collect(Collectors.toSet());
//
//        if (invalidCustomerCoordinates.size() > 0) {
//            invalidCustomerCoordinates
//                    .forEach(customerId -> errorMessage.appendMessage("Customer ID " + customerId + " has illegal location - coordinates must be in the range of [0, 50]" + System.lineSeparator()));
//            foundBadCoordinates = true;
//        }
//        return foundBadCoordinates;
//    }

    public static void postProductsStoreDataValidation(Map<Integer, Product> idToProduct, List<SDMStore> sdmStores) throws ValidationException {
        ErrorMessage errorMessage = new ErrorMessage("");
        Set<SDMStore> sdmStoresSet = new HashSet<>(sdmStores);
        boolean foundNonExistingStoresProducts = checkNonExistingStoresProducts(errorMessage, sdmStoresSet, idToProduct);
        boolean foundNonSoldProducts = checkProductsSoldByAtLeastOneStore(errorMessage, sdmStoresSet, idToProduct);
//        boolean foundNonExistingDiscountsProductIdsInStoresByAllProducts = checkNonExistingDiscountsProductIdsInStores(errorMessage, sdmStores);
        if (foundNonExistingStoresProducts || foundNonSoldProducts) {
            throw new ValidationException(errorMessage.getMessage());
        }
    }

    private static boolean checkProductsSoldByAtLeastOneStore(ErrorMessage errorMessage, Set<SDMStore> sdmStores, Map<Integer, Product> idToProduct) {
        boolean foundNonSoldProducts = false;
        // 3.5 validation - all the products are sold by at least one store
        List<Product> nonSoldProducts = getNonSoldProducts(idToProduct, sdmStores);
        errorMessage.appendMessage(nonSoldProducts.stream().map(Product::getId).map(Object::toString)
                .reduce("", (acc, currProductId) -> acc + "item id " + currProductId + " is not sold by any store" + System.lineSeparator()));

        if (!nonSoldProducts.isEmpty()) {
            foundNonSoldProducts = true;
        }

        return foundNonSoldProducts;
    }

    private static boolean checkNonExistingStoresProducts(ErrorMessage errorMessage, Set<SDMStore> sdmStores, Map<Integer, Product> idToProduct) {
        boolean foundNonExistingStoreProducts = false;
        // check for non existing products sold by stores
        Map<Integer, List<Integer>> nonValidStoresToNonExistingProds = getNonExistingProducts(idToProduct, sdmStores);
        nonValidStoresToNonExistingProds
                .forEach((storeId, productIds) -> {
                    if (productIds.size() > 0) {
                        productIds
                                .forEach(productId -> errorMessage.appendMessage("product id " + productId + " sold by store id " + storeId + " but doesn't exist" + System.lineSeparator()));
                    }
                });

        if (!nonValidStoresToNonExistingProds.isEmpty()) {
            foundNonExistingStoreProducts = true;
        }

        return foundNonExistingStoreProducts;
    }

    private static List<Product> getNonSoldProducts(Map<Integer, Product> idToProduct, Set<SDMStore> sdmStores) {
        List<Product> unSoldProducts = new ArrayList<>();
        for (Product product : new HashSet<>(idToProduct.values())) {
            boolean isSold = false;
            for (SDMStore sdmStore : sdmStores) {
                if (sdmStore.getSDMPrices().getSDMSell().stream().anyMatch(sell -> sell.getItemId() == product.getId())) {
                    isSold = true;
                    break;
                }
            }
            if (!isSold) {
                unSoldProducts.add(product);
            }
        }
        return unSoldProducts;
    }

    private static Map<Integer, List<Integer>> getDuplicateProducts(List<SDMStore> stores) {
        Map<Integer, List<Integer>> res = new HashMap<>();
        for (SDMStore sdmStore : stores) {
            Set<Integer> duplicateProductIds = findDuplicates(sdmStore.getSDMPrices().getSDMSell().stream().map(SDMSell::getItemId).collect(Collectors.toList()));
            if (duplicateProductIds.size() > 0) {
                res.put(sdmStore.getId(), new ArrayList<>(duplicateProductIds));
            }
        }
        return res;
    }

    private static Map<Integer, List<Integer>> getNonExistingProducts(Map<Integer, Product> idToProduct, Set<SDMStore> stores) {
        Map<Integer, List<Integer>> res = new HashMap<>();

        for (SDMStore sdmStore : stores) {
            Set<Integer> nonExistingProdIds = sdmStore.getSDMPrices().getSDMSell().stream()
                    .map(SDMSell::getItemId)
                    .filter(id -> !idToProduct.containsKey(id))
                    .collect(Collectors.toSet());
            if (nonExistingProdIds.size() > 0) {
                res.put(sdmStore.getId(), new ArrayList<>(nonExistingProdIds));
            }
        }
        return res;
    }

    private static <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for (T t : collection) {
            if (!uniques.add(t)) {
                duplicates.add(t);
            }
        }
        return duplicates;
    }

    private static Set<Integer> getBadCoordinatesStoresIds(Set<SDMStore> stores) {
        return stores.stream()
                .filter(store -> store.getLocation().getX() < 0 || store.getLocation().getY() < 0 || store.getLocation().getX() > 50 || store.getLocation().getY() > 50)
                .map(SDMStore::getId)
                .collect(Collectors.toSet());
    }

}
