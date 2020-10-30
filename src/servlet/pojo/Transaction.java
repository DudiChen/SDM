package servlet.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    String type;
    String date;
    int amount;
    int balanceBefore;
    int balanceAfter;

    public Transaction(String type, Date date, int amount, int balanceBefore, int balanceAfter) {
        this.type = type;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.date = formatter.format(date);
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }
}
