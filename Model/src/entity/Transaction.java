package entity;

import java.util.Date;

public class Transaction {
    private TransactionType transactionType;
    private double amount;
    private Customer issuingCustomer;
    private Customer issuedCustomer;
    private Date date;

    public Transaction(TransactionType transactionType, double amount, Date date, Customer issuingCustomer, Customer issuedCustomer) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.issuingCustomer = issuingCustomer;
        this.issuedCustomer = issuedCustomer;
    }

    public enum TransactionType {
        RECHARGE,
        EARNING,
        EXPENSE
    }
}
