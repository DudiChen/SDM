package servlet.pojo;

import servlet.util.ServletUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDTO {
    String type;
    String date;
    int amount;
    int balanceBefore;
    int balanceAfter;

    public TransactionDTO(String type, Date date, int amount, int balanceBefore, int balanceAfter) {
        this.type = type;
        this.date = ServletUtils.formatDateToString(date);
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }
}
