package servlet.pojo;

import java.util.Date;

public class Transaction {
    String type;
    Date date;
    int amount;
    int balanceBefore;
    int balanceAfter;

    public Transaction(String type, Date date, int amount, int balanceBefore, int balanceAfter) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }
}
