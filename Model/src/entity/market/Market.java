package entity.market;

import entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class Market {
    private Map<Integer, Area> idToArea;
    private Map<Integer, Customer> idToCustomer;

    public Market() {
        this.idToCustomer = new HashMap<>();
        this.idToArea = new HashMap<>();
    }

    public void addArea(Area newArea) {
        idToArea.put(newArea.getId(), newArea);
    }

    public Area getAreaById(int areaId) {
        return this.idToArea.get(areaId);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(this.idToCustomer.values());
    }

    public Customer getCustomerById(int id) { return this.idToCustomer.get(id); }

    public boolean isEmpty() {
        return (idToArea == null || idToCustomer.isEmpty()) ;
    }

    public boolean addCustomer(Customer customer) {
        if(this.getCustomerById(customer.getId()) != null) {
            return false;
        }
        else {
            this.idToCustomer.put(customer.getId(), customer);
            return true;
        }
    }

    public List<Area> getAllAreas() {
        return new ArrayList<>(idToArea.values());
    }
}
