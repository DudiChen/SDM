package controller;
import builder.AreaBuilder;
import com.sun.xml.internal.ws.util.StringUtils;
import entity.*;
import entity.Area;
import entity.OrderInvoice;
import entity.market.Market;
import entity.market.MarketUtils;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.util.Pair;
import jaxb.JaxbHandler;
import javax.management.modelmbean.XMLParseException;
import javax.websocket.Session;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Controller {
    private Market market;
    private static Controller instance;
    Map<Customer, Session> loggedInSellerToSession;

    private Controller() {
        loggedInSellerToSession = Collections.synchronizedMap(new HashMap<Customer, Session>());
        this.market = new Market();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public int getProductNumberOfSales(int areaId, int productId) {
        return this.market.getAreaById(areaId).getAllStores().stream()
                .map(store -> store.getTotalProductSales(productId))
                .reduce(0, Integer::sum);
    }

    public String loadXMLData(File xmlFile, int currentCustomerId) {
        Area area;
        Customer currentCustomer = market.getCustomerById(currentCustomerId);
        JaxbHandler jaxbHandler = new JaxbHandler();
        String message = "File uploaded successfully.";
        try {
            int newAreaId = MarketUtils.generateIdForArea(market);
            area = new
                    AreaBuilder(newAreaId, currentCustomer).build(jaxbHandler.extractXMLData(xmlFile));
            market.addArea(area);
        } catch (ValidationException | FileNotFoundException | XMLException | XMLParseException e) {
            message = e.getMessage();
        }
        return message;
    }

    private Optional<List<Pair<Integer, Double>>> findCheapestOrderForStore(Area area, Store store, List<Pair<Integer, Double>> productQuantityPairs) {

        List<Pair<Integer, Double>> res = new ArrayList<>();
        productQuantityPairs
                .forEach(pair -> {
                    area.getAllStores().stream()
                            .mapToDouble(storeToCheckPriceIn -> {
                                double price;
                                try {
                                    price = store.getPriceOfProduct(pair.getKey());
                                } catch (NoSuchElementException e) {
                                    price = Double.MAX_VALUE;
                                }
                                return price;
                            })
                            .min()
                            .ifPresent(minPrice -> {
                                try {
                                    double priceInCurrentStore = store.getPriceOfProduct(pair.getKey());
                                    if (minPrice == priceInCurrentStore) {
                                        res.add(new Pair<>(pair.getKey(), pair.getValue()));
                                    }
                                } catch (NoSuchElementException e) {

                                }
                            });
                });
        if (res.isEmpty()) {
            return Optional.empty();
        }
        System.out.println(res);
        return Optional.of(res);
    }

    private int makeOrderForStore(Area area, Store store, Date date, Integer customerId, Pair<List<Pair<Integer, Double>>, List<Discount.Offer>> productQuantityPairsWithOffers) throws OrderValidationException {
        StringBuilder err = new StringBuilder();
        List<Discount.Offer> chosenOffers = productQuantityPairsWithOffers.getValue();
        // validate store coordinate is not the same as customer coordinate
        assert false;
        if (this.market.getCustomerById(customerId).getLocation().equals(store.getLocation())) {
            err.append("cannot make order from same coordinate as store").append(System.lineSeparator());
        }
        // validate chosen products are sold by the chosen store
        for (Pair<Integer, Double> productToQuantity : productQuantityPairsWithOffers.getKey()) {
            int productId = productToQuantity.getKey();
            if (!store.isProductSold(productId)) {
                err.append(area.getProductById(productId).getName()).append(" is not sold by ").append(area.getStoreById(store.getId()).getName()).append(System.lineSeparator());
            }
        }
        if (err.length() > 0) {
            throw new OrderValidationException(err.toString());
        }

        int orderId = area.receiveOrder(new Order(customerId, productQuantityPairsWithOffers.getKey(), chosenOffers, this.market.getCustomerById(customerId).getLocation(), date, store.getId()));
        Customer orderingCustomer = this.getCustomerById(customerId);
        Customer storeOwner = this.getCustomerByName(store.getOwnerName());
        storeOwner.addNotification(orderingCustomer.getName() + " ordered from " + store.getName());
        this.notifyLoggedInSellers(storeOwner);
        return orderId;
    }

    public Product getAreaProductById(int areaId, int productId) {
        return this.market.getAreaProductById(areaId, productId);
    }

    public Customer getCustomerById(int uuid) {
        return this.market.getCustomerById(uuid);
    }

    public List<entity.Transaction> getTransactionsForCustomer(int uuid) {
        return this.market.getCustomerById(uuid).getAllTransactions();
    }

    public void rechargeCustomerBalance(int uuid, double amount, Date date) {
        Customer customer = this.market.getCustomerById(uuid);
        entity.Transaction transaction = new entity.Transaction(entity.Transaction.TransactionType.RECHARGE, amount, date, customer, customer);
        this.market.getCustomerById(uuid).addTransaction(transaction);
    }

    public void addNewStoreToArea(int uuid, int areaId, String storeName, Point point, Map<String, Integer> productIdToPriceInNewStore, double ppk) {
        Customer customer = this.market.getCustomerById(uuid);
        Map<Integer, StoreProduct> stockProducts = new HashMap<>();
        for (Map.Entry<String, Integer> entry : productIdToPriceInNewStore.entrySet()) {
            int integerId = Integer.parseInt(entry.getKey());
            StoreProduct newProduct = new StoreProduct(this.getAreaProductById(areaId, integerId), entry.getValue());
            stockProducts.put(integerId, newProduct);
        }
        Stock stock = new Stock(stockProducts);
        Store newStore = new Store(point, stock, (int) ppk, MarketUtils.generateIdForStore(Controller.getInstance().getAreaById(areaId)), storeName, areaId, customer.getName(), new HashMap<>());
        this.market.getAreaById(areaId).addStore(newStore);


        Customer areaOwner = this.getCustomerByName(this.getAreaById(areaId).getOwnerName());
        Customer consumer = this.getCustomerById(uuid);
        areaOwner.addNotification(consumer.getName() + " opened a new store in your area");
        this.notifyLoggedInSellers(areaOwner);
    }

    public Area getAreaById(int areaId) {
        return this.market.getAreaById(areaId);
    }

    public List<Store> getAllStoresInArea(int areaId) {
        return this.market.getAreaById(areaId).getAllStores();
    }

    public List<StoreProduct> getAllProductsInStore(int areaId, int storeId) {
        return new ArrayList<>(this.market.getAreaById(areaId).getStoreById(storeId).getStock().getSoldProducts().values());
    }

    public List<Discount> getDiscountsInStoreByProductId(int areaId, int storeId, int productId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getDiscountsByProductId(productId);
    }

    public List<OrderInvoice> getAllOrdersForStore(int areaId, int storeId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getOrdersHistory();
    }

    public List<InvoiceProduct> getAllProductsInStoreOrder(int areaId, int storeId, int orderId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getOrdersHistory().stream()
                .filter(orderInvoice -> orderInvoice.getOrderId() == orderId)
                .findAny().get().getInvoiceProducts();
    }

    public List<InvoiceDiscountProduct> getAllDiscountProductsInStoreOrder(int areaId, int storeId, int orderId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getOrdersHistory().stream()
                .filter(orderInvoice -> orderInvoice.getOrderId() == orderId)
                .findAny().get().getDiscountProducts();
    }

    public void addStoreFeedback(int uuid, int areaId, int storeId, int rating, String text) {
        Feedback feedback = new Feedback(this.market.getCustomerById(uuid).getName(), rating, text);
        this.market.getAreaById(areaId).getStoreById(storeId).addFeedback(feedback);
        Customer storeOwner = this.getCustomerByName(this.getAreaById(areaId).getStoreById(storeId).getOwnerName());
        Customer consumer = this.getCustomerById(uuid);
        storeOwner.addNotification(consumer.getName() + " gave a feedback on your store");
        this.notifyLoggedInSellers(storeOwner);
    }

    // TODO: NOAM: Why do we init customer with (0,0) location and not get it from argument?
    public boolean addCustomer(String username, String role) {
        Customer customer = new Customer(MarketUtils.generateId(), username, new Point(0, 0), Customer.Role.valueOf(role.toUpperCase()));
        return this.market.addCustomer(customer);
    }

    public Customer getCustomerByName(String userName) {
        return this.market.getAllCustomers().stream()
                .filter(customer -> customer.getName().equals(userName)).findAny().orElse(null);
    }

    public double getBalanceByCustomerId(int uuid) {
        return this.market.getBalanceByCustomerId(uuid);
    }

    public List<Area> getAllAreas() {
        return this.market.getAllAreas();
    }

    public double getAverageProductPrice(int areaId, int productId) {
        return this.market.getAreaAverageProductPrice(areaId, productId);
    }

    public double getAreaAverageOrderCost(int areaId) {
        return this.market.getAreaAverageOrderCost(areaId);
    }

    public List<Product> getAllProductInArea(int areaId) {
        return this.market.getAreaById(areaId).getAllProducts();
    }

    public List<Feedback> getStoreFeedbacks(int areaId, int storeId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getAllFeedbacks();
    }

    public int getNumberOfStoresThatSellProduct(int areaId, int productId) {
        return this.market.getNumberOfStoresThatSellProduct(areaId, productId);
    }

    public void logInSeller(Session session, Customer customer) {
        this.loggedInSellerToSession.put(customer, session);
    }

    public void logOutSeller(Customer customer) {
        this.loggedInSellerToSession.remove(customer);
    }

    public void notifyLoggedInSellers(Customer... customers) {
        Stream.of(customers).forEach(customer -> {
            if (this.loggedInSellerToSession.containsKey(customer)) {
                Session loggedInSellerSession = this.loggedInSellerToSession.get(customer);
                try {
                    loggedInSellerSession.getBasicRemote().sendText("new notifications");
                } catch (IOException e) {
                    // TODO: Decide what to do in case of exception
                    e.printStackTrace();
                }
            } else {
                System.out.println("customer " + customer + " is not a logged in seller - therefore was not notified");
            }
        });
    }

    public int generateAreaId() {
        return MarketUtils.generateIdForArea(market);
    }

    public List<Discount> getAvailableDiscounts(int areaId, int storeId, Map<String, List<Integer>> discountNameToProductIdInOffer, Map<Integer, Double> productIdToQuantity) {
        List<Pair<Integer, Double>> productIdQuantityPairs = productIdToQuantity.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        List<Discount> allMatchingDiscounts = this.market.getAreaById(areaId)
                .getStoreById(storeId)
                .getMatchingDiscountsByProductIdQuantityPairs(productIdQuantityPairs);
        for (String discountName : discountNameToProductIdInOffer.keySet()) {
            OptionalInt firstFoundIndex = IntStream.range(0, allMatchingDiscounts.size()).filter(i -> allMatchingDiscounts.get(i).getName().equals(discountName)).findFirst();
            firstFoundIndex.ifPresent(index -> allMatchingDiscounts.remove(index));
        }
        return allMatchingDiscounts;
    }

    public List<Customer> getAllCustomers() {
        return this.market.getAllCustomers();
    }

    public List<Integer> orderFromArea(int uuid, int areaId, Date date, Map<Integer, Double> productIdToQuantity) throws OrderValidationException {

        List<Pair<Integer, Double>> productIdQuantityPairs = productIdToQuantity.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        Pair<List<Pair<Integer, Double>>, List<Discount.Offer>> productQuantityPairs = new Pair<>(productIdQuantityPairs, new ArrayList<>());
        List<Integer> orderIds = new ArrayList<>();
        for (Store store : this.market.getAreaById(areaId).getAllStores()) {
            Optional<List<Pair<Integer, Double>>> maybeOrder = findCheapestOrderForStore(this.getAreaById(areaId), store, productQuantityPairs.getKey());
            if (maybeOrder.isPresent()) {
                List<Pair<Integer, Double>> orderPairs = maybeOrder.get();
                List<Pair<Integer, Double>> toDelete = new ArrayList<>();
                for (Pair<Integer, Double> pair : productQuantityPairs.getKey()) {
                    for (Pair<Integer, Double> pair1 : orderPairs) {
                        if (pair.getKey() == pair1.getKey()) {
                            toDelete.add(pair);
                            break;
                        }
                    }
                }
                productQuantityPairs.getKey().removeAll(toDelete);
                orderIds.add(makeOrderForStore(this.market.getAreaById(areaId), store, date, uuid, new Pair<>(orderPairs, productQuantityPairs.getValue())));
            }
        }
        return orderIds;
    }

    public int performOrderForStore(int uuid, int areaId, int storeId, Date date, Map<String, List<Integer>> discountNameToProductIdInOffer, Map<Integer, Double> productIdToQuantity) throws OrderValidationException {
        // insert order
        List productIdToQuantityPairsList = productIdToQuantity.entrySet().stream().map(entry -> new Pair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        List<Discount.Offer> offers = this.market.getAreaById(areaId).getStoreOfferListByDiscountNameToProductIdInOffer(storeId, discountNameToProductIdInOffer);
         Pair<List<Pair<Integer, Double>> , List<Discount.Offer>> productQuantityPairsWithOffers = new Pair<>(productIdToQuantityPairsList, offers);
        Area area = this.getAreaById(areaId);
        Store store = area.getStoreById(storeId);
        int orderId = this.makeOrderForStore(area, store, date, uuid, productQuantityPairsWithOffers);
        return orderId;
    }

    public List<String> getSellerNotifications(int uuid) {
        return this.getCustomerById(uuid).getNotifications();
    }

    public StoreProduct getStoreProductById(int areaId, int storeId, int productId) {
        return this.market.getStoreProductById(areaId, storeId, productId);
    }
}