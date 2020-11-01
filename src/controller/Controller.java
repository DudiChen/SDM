package controller;

import builder.AreaBuilder;
import builder.MarketBuilder;
import command.Executor;
import entity.*;
import entity.Area;
import entity.OrderInvoice;
import entity.market.Market;
import entity.market.Market;
import entity.market.MarketUtils;
import exception.DiscountsRemovedException;
import exception.MarketIsEmptyException;
import exception.OrderValidationException;
import exception.XMLException;
import javafx.util.Pair;
import jaxb.JaxbHandler;
import view.ApplicationContext;
import view.View;
import view.menu.item.CustomerMapElement;
import view.menu.item.StoreMapElement;

import javax.management.modelmbean.XMLParseException;
import javax.transaction.Transaction;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
//    private Executor executor;
    private AtomicReference<Store> chosenStore;
    private boolean loaded = false;
    private Market market;
    private int currentCustomerId;
    private static Controller instance;

    private Controller() {
        this.market = new Market();
//        this.executor = new Executor(this);
        this.chosenStore = new AtomicReference<>();
        registerToViewEvents();
    }

    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
        }
        return instance;
    }

//    public void fetchAllStoresToUI() {
//        List<Store> stores = new ArrayList<>();
//        if (market == null || market.isEmpty()) {
//            view.displayStores(stores);
//            return;
//        }
//        stores = this.market.getAllStores();
//        view.displayStores(stores);
//    }
    public int getProductNumberOfSales(int areaId, int productId) {
        return this.market.getAreaById(areaId).getAllStores().stream()
                .map(Store::getTotalProductSales)
                .sum();
    }

    public void fetchAllStoresToUI() {
        List<Store> stores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayStores(stores);
            return;
        }
        stores = this.market.getAllStores();
        view.displayStores(stores);
    }

//    public void loadXMLDataToUI() {
//        String xmlPath = view.promptUserFilePath();
//        loadXMLData(xmlPath);
//    }

    public void loadXMLData(File xmlFile) {
        JaxbHandler jaxbHandler = new JaxbHandler();
        try {
            market =  new MarketBuilder().build(jaxbHandler.extractXMLData(xmlFile));
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (XMLException e) {
            // TODO: Decide what to do with XMLException
        } catch (FileNotFoundException e) {
            // TODO: Decide what to do with FileNotFountException - if relevant
        } catch (XMLParseException e) {
            // TODO: Decide what to do with XMLParseException
        }
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

    public void addNewProduct(int storeId, int productId) {
        this.market.addProductToStore(storeId, productId, 0);
        this.view.onStoreIdChoice.accept(storeId);
    }

    public void fetchAllProductsToUI() {
        List<Product> products = new ArrayList<>();
        List<Store> allStores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayProducts(products, allStores);
            return;
        }
        products = market.getAllProducts();
        allStores = market.getAllStores();
        view.displayProducts(products, allStores);
    }


    public void getCustomerToUI() {
    }

    private void registerToViewEvents() {
        registerOnStoreChoice();
        registerOnOrderPlaced();
        registerOnOrderAccepted();
        registerOnOrderCanceled();
        registerOnDynamicOrder();
    }

    private void registerOnDynamicOrder() {
        view.onDynamicOrder = (date, customerId, productQuantityPairs) -> {
            this.market.getAllStores()
                    .forEach(store -> {
                        Optional<List<Pair<Integer, Double>>> maybeOrder = findCheapestOrderForStore(store, productQuantityPairs.getKey());
                        if (maybeOrder.isPresent()) {
                            this.chosenStore.set(store);
                            List<Pair<Integer, Double>> orderPairs = maybeOrder.get();
                            try {
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
                                makeOrderForChosenStore(date, customerId, new Pair<>(orderPairs, productQuantityPairs.getValue()));
                            } catch (OrderValidationException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            view.displayMessage("Please Confirm / Cancel All Orders");
        };
    }

    private Optional<List<Pair<Integer, Double>>> findCheapestOrderForStore(Store store, List<Pair<Integer, Double>> productQuantityPairs) {

        List<Pair<Integer, Double>> res = new ArrayList<>();
        productQuantityPairs
                .forEach(pair -> {
                    this.market.getAllStores().stream()
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

    private void registerOnOrderAccepted() {
        view.onOrderAccepted = orderInvoiceId -> market.approveOrder(orderInvoiceId);
    }

    private void registerOnOrderCanceled() {
        view.onOrderCanceled = (orderInvoiceId) -> market.cancelOrder(orderInvoiceId);
    }

    private void registerOnStoreChoice() {
        view.onStoreIdChoice = (storeId) -> {
            assert false;
            chosenStore.set(market.getStoreById(storeId));
            fetchAllProductsListToUI();
        };
    }

    private void registerOnOrderPlaced() {
        view.onOrderPlaced = this::makeOrderForChosenStore;
    }

//    TODO::UI: Missing the Offers support from order when displaying invoice.
//    TODO::UI: in the Order History, under Orders tab; the Discount offer items are not counted - need to add as "Number of items from Discounts" => use getNumberOfDiscountProducts() & getNumberOfInvoiceProducts().
//    TODO::UI: Orders does not display customer related data
//    TODO::UI: On Dynamic Order display; missing prompt to user about order split info before viewing multiple orders
    private void makeOrderForChosenStore(Date date, Integer customerId, Pair<List<Pair<Integer, Double>>, List<Discount.Offer>> productQuantityPairsWithOffers) throws OrderValidationException {
        StringBuilder err = new StringBuilder();
        List<Discount.Offer> chosenOffers = productQuantityPairsWithOffers.getValue();
        // validate store coordinate is not the same as customer coordinate
        assert false;
        if (this.market.getCustomerById(customerId).getLocation().equals(chosenStore.get().getLocation())) {
            err.append("cannot make order from same coordinate as store").append(System.lineSeparator());
        }
        // validate chosen products are sold by the chosen store
        for (Pair<Integer, Double> productToQuantity : productQuantityPairsWithOffers.getKey()) {
            int productId = productToQuantity.getKey();
            if (!chosenStore.get().isProductSold(productId)) {
                err.append(market.getProductById(productId).getName()).append(" is not sold by ").append(market.getStoreById(chosenStore.get().getId()).getName()).append(System.lineSeparator());
            }
        }
        if (err.length() > 0) {
            throw new OrderValidationException(err.toString());
        }
        if (productQuantityPairsWithOffers.getKey().size() == 0) {
            view.showMainMenu();
        }
        int orderInvoiceId = market.receiveOrder(new Order(customerId, productQuantityPairsWithOffers.getKey(), chosenOffers, this.market.getCustomerById(customerId).getLocation(), date, chosenStore.get().getId()));
        view.summarizeOrder(market.getOrderInvoice(orderInvoiceId));
    }

    // TODO :: make an alert when dynamic order is more than 2 stores long
    public void makeDynamicOrder() {
        fetchAllProductsListToUI();
    }

    public void makeOrder() {
        fetchAllStoresListToUI();
    }

    public void fetchAllStoresListToUI() {
        List<Store> stores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayStoresList(stores);
            return;
        }
        stores = market.getAllStores();
        view.displayStoresList(stores);
    }

    public void fetchAllProductsListToUI() {
        List<Product> products = new ArrayList<>();
        List<Store> allStores = new ArrayList<>();
        if (market == null || market.isEmpty()) {
            view.displayProductsForStore(products, chosenStore.get());
            return;
        }
        products = market.getAllProducts()
                .stream().filter(product -> chosenStore.get().isProductSold(product.getId())).collect(Collectors.toList());
        allStores = market.getAllStores();
        view.displayProductsForStore(products, chosenStore.get());
    }

    public void fetchOrdersHistoryToUI() {
        try {
            view.showOrdersHistory(market.getOrdersHistory());
        } catch (MarketIsEmptyException e) {
            view.displayError("Please Load XML First");
        }
    }

    public Executor getExecutor() {
        return executor;
    }

    public void run() {
        this.view.showMainMenu();
    }

    public String getStoreNameByID(int storeId) {
        return this.market.getStoreById(storeId).getName();
    }

    public void saveOrderHistory() {
        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
            view.displayError("Please Load XML File Before");
            return;
        }
        try {
            List<OrderInvoice> history = market.getOrdersHistory();
            if (history.size() == 0) {
                view.displayError("No History To Show");
            } else {
                String outputPath = view.promptUserFilePath();
                try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
                    out.writeObject(history);
                    view.fileLoadedSuccessfully();
                } catch (FileNotFoundException e) {
                    view.displayError("File Not Found");

                } catch (IOException e) {
                    view.displayError("Error While Writing To File");
                }
            }
        } catch (MarketIsEmptyException e) {
            view.displayError("Load XML First");
        }

    }

    public void loadOrderHistory() {
        if (market.isEmpty()) { // TODO: this is a patch - the check should be if the market has the sufficient data - fix for ex2 !
            view.displayError("Please Load XML File Before");
            return;
        }
        String inputPath = view.promptUserFilePath();
        try (InputStream inputStream = new FileInputStream(inputPath)) {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            List<OrderInvoice> history = (ArrayList<OrderInvoice>) in.readObject();
            market.setOrdersHistory(history);
            view.fileLoadedSuccessfully();
        } catch (FileNotFoundException e) {
            view.displayError("File Not Found");
        } catch (IOException e) {
            view.displayError("Error While Writing To File");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Store getStoreById(int storeId) {
        return this.market.getStoreById(storeId);
    }

    public void fetchMapToUI() {
        this.view.showMap(
                Stream.concat(
                        this.market.getAllStores().stream()
                                .map(store -> new StoreMapElement(store, view.onStoreIdChoice)),
                        this.market.getAllCustomers().stream()
                                .map(customer -> new CustomerMapElement(customer))
                )
                        .collect(Collectors.toList()));
    }

    public Product getProductById(int productId) {
        return this.market.getProductById(productId);
    }

    public boolean isAvailableDiscount(Discount discount, List<Pair<Integer, Double>> orderProducts, List<Discount> chosenDiscounts) {
        int timesUsedDiscount = Collections.frequency(chosenDiscounts, discount);
        boolean isAvailable = this.market.isAvailableDiscount(discount, orderProducts, timesUsedDiscount);
        if (!isAvailable) {
            view.displayMessage(
                    String.format("%s: Not eligible %s according to current product quantity.",
                    discount.getName(),
                    (timesUsedDiscount > 0) ? "for another discount" : ""));
        }
        return isAvailable;
    }

    public List<Customer> getAllCustomers() {
        return this.market.getAllCustomers();
    }

    public Customer getCustomerById(int customerId) {
        return this.market.getCustomerById(customerId);
    }

    public void deleteProduct(int productId, int storeId) {
        try {
            this.market.deleteProductForStore(productId, storeId);
            ApplicationContext.getInstance().navigateBack();
            this.view.onStoreIdChoice.accept(storeId);
        }
        catch (ValidationException | DiscountsRemovedException e) {
            this.view.displayMessage(e.getMessage());
        }
    }

    public void changePriceForProduct(int storeId, int productId, double newPrice) {
        this.market.changePriceForProduct(storeId, productId, newPrice);
    }

    public List<Product> getAllProducts() {
        return this.market.getAllProducts();
    }

    // ex3
    public List<Transaction> getTransactionsForCustomer(int uuid) {
        return this.market.getCustomerById(uuid).getAllTransactions();
    }

    public void rechargeCustomerBalance(int uuid, double amount, Date date) {
        Customer customer = Controller.getInstance().getCustomerById(uuid);
        entity.Transaction transaction = new entity.Transaction(entity.Transaction.TransactionType.RECHARGE, amount, date, customer, customer);
        this.market.getCustomerById(uuid).addTransaction(transaction);
    }

    public void addNewStoreToArea(int uuid, int areaId, String storeName, Point point, Map<String, Integer> productIdToPriceInNewStore, double ppk) {
        Customer customer = this.getCustomerById(uuid);
        Map<Integer, StoreProduct> stockProducts = new HashMap<>();
        for(Map.Entry<String, Integer> entry : productIdToPriceInNewStore.entrySet()) {
            int integerId = Integer.parseInt(entry.getKey());
            StoreProduct newProduct = new StoreProduct(Controller.getInstance().getProductById(integerId), entry.getValue());
            stockProducts.put(integerId, newProduct);
        }
        Stock stock = new Stock(stockProducts);
        Store newStore = new Store(point, stock, ppk, MarketUtils.generateIdForStore(Controller.getInstance().getAreaById(areaId)), storeName, new ArrayList());
        this.market.getAreaById(areaId).addStore(newStore);
    }

    private Area getAreaById(int areaId) {
        this.market.getAreaById(areaId);
    }

    public List<Store> getAllStoresInArea(int areaId) {
        return this.market.getAreaById(areaId).getAllStores();
    }

    public List<StoreProduct> getAllProductsInStore(int areaId, int storeId) {
        return this.market.getAreaById(areaId).getStoreById().getStock().getSoldProducts().values();
    }

    public List<Discount> getDiscountsInStoreByProductId(int areaId, int storeId, int productId) {
        return this.market.getAreaById(areaId).getStoreById().getDiscountsByProductId(productId);
    }

    public List<OrderInvoice> getAllOrdersForStore(int areaId, int storeId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getOrdersHistory();
    }

    public List<InvoiceProduct> getAllProductsInStoreOrder(int areaId, int storeId, int orderId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getOrdersHistory().stream()
                .filter(orderInvoice -> orderInvoice.getOrderId() == orderId)
                .findAny().get().getInvoiceProducts();
    }

    public void addStoreFeedback(int uuid, int areaId, int storeId, int rating, String text) {
        Feedback feedback = new Feedback(this.getCustomerById(uuid).getName(), rating, text);
        this.market.getAreaById(areaId).getStoreById(storeId).addFeedback(feedback);
    }

    public boolean addCustomer(String username, String role) {
        Customer customer = new Customer(MarketUtils.generateId(), username, new Point(0, 0));
        return this.market.addCustomer(customer);
    }

    public Customer getCustomerByName(String userName) {
        return this.market.getAllCustomers().stream()
                .filter(customer -> customer.getName().equals(userName)).findAny().orElse(null);
    }

    public String getBalanceByCustomerId(int uuid) {
        return this.market.getCustomerById(uuid).getBalance();
    }

    public List<Area> getAllAreas() {
        return this.market.getAllAreas();
    }

    public double getAverageProductPrice(int areaId, int productId) {
        return this.market.getAreaById(areaId).getAllStores().stream()
                .map(store -> store.getPriceOfProduct(productId))
                .mapToDouble(x->x).average().orElse(0);
    }

    public List<Product> getAllProductInArea(int areaId) {
        return this.market.getAreaById(areaId).getAllProducts();
    }

    public List<Feedback> getStoreFeedbacks(int areaId, int storeId) {
        return this.market.getAreaById(areaId).getStoreById(storeId).getAllFeedbacks();
    }

    public int getNumberOfStoresThatSellProduct(int areaId, int productId) {
        return this.market.getAreaById(areaId).getAllStores().stream()
                .map(store -> store.isProductSold(productId) ? 1 : 0)
                .mapToInt(x->x)
                .sum();
    }
}