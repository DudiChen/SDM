package controller;

import builder.AreaBuilder;
//import builder.MarketBuilder;
//import command.Executor;
import entity.*;
import entity.Area;
import entity.OrderInvoice;
import entity.market.Market;
import entity.market.MarketUtils;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.util.Pair;
import jaxb.JaxbHandler;
//import view.ApplicationContext;
//import view.View;
//import view.menu.item.CustomerMapElement;
//import view.menu.item.StoreMapElement;

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
    //    private Executor executor;
    private boolean loaded = false;
    private Market market;
    private int currentCustomerId;
    private static Controller instance;
    Map<Customer, Session> loggedInSellerToSession;

    private Controller() {
        loggedInSellerToSession = Collections.synchronizedMap(new HashMap<Customer, Session>());
        this.market = new Market();
//        this.executor = new Executor(this);
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void fetchAllStoresToUI() {
//        List<Store> stores = new ArrayList<>();
//        if (market == null || market.isEmpty()) {
//            view.displayStores(stores);
//            return;
//        }
//        stores = this.market.getAllStores();
//        view.displayStores(stores);
    }

    public int getProductNumberOfSales(int areaId, int productId) {
        return this.market.getAreaById(areaId).getAllStores().stream()
                .map(store -> store.getTotalProductSales(productId))
                .reduce(0, Integer::sum);
    }

//    public void fetchAllStoresToUI() {
////        List<Store> stores = new ArrayList<>();
////        if (market == null || market.isEmpty()) {
////            view.displayStores(stores);
////            return;
////        }
////        stores = this.market.getAllStores();
////        view.displayStores(stores);
//    }

//    public void loadXMLDataToUI() {
//        String xmlPath = view.promptUserFilePath();
//        loadXMLData(xmlPath);
//    }

    public String loadXMLData(File xmlFile, int currentCustomerId) {
        Area area;
        Customer currentCustomer = market.getCustomerById(currentCustomerId);
        JaxbHandler jaxbHandler = new JaxbHandler();
        String message = "File uploaded successfully.";
        try {
            int newAreaId = MarketUtils.generateIdForArea(market);
            area = new AreaBuilder(newAreaId, currentCustomer).build(jaxbHandler.extractXMLData(xmlFile));
            market.addArea(area);
        } catch (ValidationException | FileNotFoundException | XMLException | XMLParseException e) {
            message = e.getMessage();
        }
        return message;
//        LoadXmlTask loadXmlTask = new LoadXmlTask(fullFilePath);
//        loadXmlTask.setOnSucceeded(event -> {
//            try {
//                this.market = loadXmlTask.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//            view.fileLoadedSuccessfully();
//            view.showMainMenu();
//        });
//        loadXmlTask.setOnFailed(event -> {
//            Throwable exception = loadXmlTask.getException();
//            if (exception instanceof ValidationException) {
//                view.displayError(exception.getMessage());
//            }
//            else if (exception instanceof XMLParseException || exception instanceof XMLException || exception instanceof FileNotFoundException) {
//                view.displayError("File Not Found");
//            } else {
//                view.displayError("Couldn't Parse XML due to unknown error:" + exception.getMessage() + System.lineSeparator());
//            }
//        });
//        this.bindTaskToUIComponents(loadXmlTask);
//        new Thread(loadXmlTask).start();
    }

//    private void bindTaskToUIComponents(LoadXmlTask loadXmlTask) {
//        // task message
//        view.xmlProgressStateProperty().bind(loadXmlTask.messageProperty());
//        view.xmlProgressBarProperty().bind(loadXmlTask.progressProperty());
//
//        loadXmlTask.valueProperty().addListener((observable, oldValue, newValue) -> {
//            view.xmlProgressStateProperty().unbind();
//            view.xmlProgressBarProperty().unbind();
//        });
//    }

//    public void addNewProduct(int storeId, int productId) {
//        this.market.addProductToStore(storeId, productId, 0);
//        this.view.onStoreIdChoice.accept(storeId);
//    }

//    public void fetchAllProductsToUI() {
//        List<Product> products = new ArrayList<>();
//        List<Store> allStores = new ArrayList<>();
//        if (market == null || market.isEmpty()) {
//            view.displayProducts(products, allStores);
//            return;
//        }
//        products = market.getAllProducts();
//        allStores = market.getAllStores();
//        view.displayProducts(products, allStores);
//    }


//    public void getCustomerToUI() {
//    }

//    private void registerToViewEvents() {
//        registerOnStoreChoice();
//        registerOnOrderPlaced();
//        registerOnOrderAccepted();
//        registerOnOrderCanceled();
//        registerOnDynamicOrder();
//    }

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

//    private void registerOnOrderAccepted() {
//        // view.onOrderAccepted = orderInvoiceId -> market.approveOrder(orderInvoiceId);
//    }
//
//    private void registerOnOrderCanceled() {
//        // view.onOrderCanceled = (orderInvoiceId) -> market.cancelOrder(orderInvoiceId);
//    }
//
//    private void registerOnStoreChoice() {
////        view.onStoreIdChoice = (storeId) -> {
////        assert false;
////        chosenStore.set(market.getStoreById(storeId));
////        fetchAllProductsListToUI();
// //       };
//    }
//
//    private void registerOnOrderPlaced() {
//        // view.onOrderPlaced = this::makeOrderForChosenStore;
//    }

    //    TODO::UI: Missing the Offers support from order when displaying invoice.
//    TODO::UI: in the Order History, under Orders tab; the Discount offer items are not counted - need to add as "Number of items from Discounts" => use getNumberOfDiscountProducts() & getNumberOfInvoiceProducts().
//    TODO::UI: Orders does not display customer related data
//    TODO::UI: On Dynamic Order display; missing prompt to user about order split info before viewing multiple orders
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
//        view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
//    }
//
//    // TODO :: make an alert when dynamic order is more than 2 stores long
//    public void makeDynamicOrder() {
//        fetchAllProductsListToUI();
    }

//    public void makeOrder() {
//        fetchAllStoresListToUI();
//    }

//    public void fetchAllStoresListToUI() {
//        List<Store> stores = new ArrayList<>();
//        if (market == null || market.isEmpty()) {
//            view.displayStoresList(stores);
//            return;
//        }
//        stores = market.getAllStores();
//        view.displayStoresList(stores);
//    }

//    public void fetchAllProductsListToUI() {
//        List<Product> products = new ArrayList<>();
//        List<Store> allStores = new ArrayList<>();
//        if (market == null || market.isEmpty()) {
//            view.displayProductsForStore(products, chosenStore.get());
//            return;
//        }
//        products = market.getAllProducts()
//                .stream().filter(product -> chosenStore.get().isProductSold(product.getId())).collect(Collectors.toList());
//        allStores = market.getAllStores();
//        view.displayProductsForStore(products, chosenStore.get());
//    }
//
//    public void fetchOrdersHistoryToUI() {
//        try {
//            view.showOrdersHistory(market.getOrdersHistory());
//        } catch (MarketIsEmptyException e) {
//            view.displayError("Please Load XML First");
//        }
//    }
//
//    public Executor getExecutor() {
//        return executor;
//    }
//
//    public void run() {
//        this.view.showMainMenu();
//    }

//    public Store getStoreById(int storeId) {
//        return this.market.getStoreById(storeId);
//    }

//    public String getStoreNameByID(int storeId) {
//        return this.market.getStoreById(storeId).getName();
//    }

    public void saveOrderHistory() {
//        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
//            view.displayError("Please Load XML File Before");
//            return;
//        }
//        try {
//            List<OrderInvoice> history = market.getOrdersHistory();
//            if (history.size() == 0) {
//                view.displayError("No History To Show");
//            } else {
//                String outputPath = view.promptUserFilePath();
//                try (OutputStream outputStream = new FileOutputStream(outputPath)) {
//                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
//                    out.writeObject(history);
//                    view.fileLoadedSuccessfully();
//                } catch (FileNotFoundException e) {
//                    view.displayError("File Not Found");
//
//                } catch (IOException e) {
//                    view.displayError("Error While Writing To File");
//                }
//            }
//        } catch (MarketIsEmptyException e) {
//            view.displayError("Load XML First");
//        }

    }

    public void loadOrderHistory() {
//        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
//            view.displayError("Please Load XML File Before");
//            return;
//        }
//        String inputPath = view.promptUserFilePath();
//        try (InputStream inputStream = new FileInputStream(inputPath)) {
//            ObjectInputStream in = new ObjectInputStream(inputStream);
//            List<OrderInvoice> history = (ArrayList<OrderInvoice>) in.readObject();
//            market.setOrdersHistory(history);
//            view.fileLoadedSuccessfully();
//        } catch (FileNotFoundException e) {
//            view.displayError("File Not Found");
//        } catch (IOException e) {
//            view.displayError("Error While Writing To File");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }


//    public void fetchMapToUI() {
//        this.view.showMap(
//                Stream.concat(
//                        this.market.getAllStores().stream()
//                                .map(store -> new StoreMapElement(store, view.onStoreIdChoice)),
//                        this.market.getAllCustomers().stream()
//                                .map(customer -> new CustomerMapElement(customer))
//                )
//                        .collect(Collectors.toList()));
//    }

//    public Product getProductById(int productId) {
//        return this.market.getProductById(productId);
//    }

    public Product getAreaProductById(int areaId, int productId) {
        return this.market.getAreaProductById(areaId, productId);
    }

//    public boolean isAvailableDiscount(Discount discount, List<Pair<Integer, Double>> orderProducts, List<Discount> chosenDiscounts) {
//        int timesUsedDiscount = Collections.frequency(chosenDiscounts, discount);
//        boolean isAvailable = this.market.isAvailableDiscount(discount, orderProducts, timesUsedDiscount);
//        if (!isAvailable) {
//            view.displayMessage(
//                    String.format("%s: Not eligible %s according to current product quantity.",
//                            discount.getName(),
//                            (timesUsedDiscount > 0) ? "for another discount" : ""));
//        }
//        return isAvailable;
//    }
//
//    public List<Customer> getAllCustomers() {
//        return this.market.getAllCustomers();
//    }
//
//    public Customer getCustomerById(int customerId) {
//        return this.market.getCustomerById(customerId);
//    }

//    public void deleteProduct(int productId, int storeId) {
//        try {
//            this.market.deleteProductForStore(productId, storeId);
//            ApplicationContext.getInstance().navigateBack();
//            this.view.onStoreIdChoice.accept(storeId);
//        } catch (ValidationException | DiscountsRemovedException e) {
//            this.view.displayMessage(e.getMessage());
//        }
//    }

//    public void changePriceForProduct(int storeId, int productId, double newPrice) {
//        this.market.changePriceForProduct(storeId, productId, newPrice);
//    }

//    public List<Product> getAllProducts() {
//        return this.market.getAllProducts();
//    }

    // ex3
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
        Customer customer = new Customer(MarketUtils.generateId(), username, new Point(0, 0), Customer.Role.valueOf(role));
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
        List<Discount> allMatchingDiscounts = this.market.getAreaById(areaId).getStoreById(storeId).getMatchingDiscountsByProductIdQuantityPairs(productIdQuantityPairs);
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
        for(Store store : this.market.getAreaById(areaId).getAllStores()) {
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

    public void performOrderForStore(int uuid, int areaId, int storeId, Date date, Map<String, List<Integer>> discountNameToProductIdInOffer, Map<Integer, Double> productIdToQuantity) {
        // insert order
        List productIdToQuantityPairsList = productIdToQuantity.entrySet().stream().map(entry -> new Pair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        List<Discount.Offer> offers =
                // Pair<List<Pair<Integer, Double>> , List<Offer>>
                this.makeOrderForStore(date, uuid, )
        // issue notifications
    }

    public List<String> getSellerNotifications(int uuid) {
        return this.getCustomerById(uuid).getNotifications();
    }
}