package entity;

import java.awt.*;

public class Customer {
    private Role role;
    private int id;
    private String name;
    private double balance;
    private Point location;
    private int totalOrders = 0;
    private double averageOrderCost = 0;
    private double averageShipmentCost = 0;

    public Customer(int id, String name, Point location, Role role) {  //, int totalOrders, double averageOrderCost, double averageShipmentCost) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.location = location;
//        this.totalOrders = totalOrders;
//        this.averageOrderCost = averageOrderCost;
//        this.averageShipmentCost = averageShipmentCost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getAverageOrderCost() {
        return averageOrderCost;
    }

    public double getAverageShipmentCost() {
        return averageShipmentCost;
    }

    public void addOrder(int orderCost, int shipmentCost) {
        this.averageOrderCost = calcAverageValue(this.averageOrderCost, this.totalOrders, orderCost);
        this.averageShipmentCost = calcAverageValue(this.averageShipmentCost, this.totalOrders, shipmentCost);
        this.totalOrders++;
    }

    private double calcAverageValue(double averageVariable, int prevTotalInstances, double newValue) {
        return ((averageVariable * prevTotalInstances) + newValue) / (prevTotalInstances + 1);
    }

    @Override
    public String toString() {
        return this.id + ": " + this.name;
    }

    public Role getRole() {
        return role;
    }

    public double getBalance() {
        return this.balance;
    }

    public enum Role {
        SELLER("Seller"),
        CONSUMER("Consumer");

        private final String name;

        private Role(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.getName();
        }

    }

}
