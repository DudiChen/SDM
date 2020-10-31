package entity.market;

import entity.*;

import java.util.*;

public class Market {
    private Map<Integer, Area> idToArea;
    private Map<Integer, Customer> idToCustomer;

    public Market() {
        this.idToCustomer = new HashMap<>();
        this.idToArea = new HashMap<>();
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(this.idToCustomer.values());
    }

    public Customer getCustomerById(int id) { return this.idToCustomer.get(id); }

    public boolean isEmpty() {
        return (idToArea == null || idToCustomer.isEmpty()) ;
    }
}
