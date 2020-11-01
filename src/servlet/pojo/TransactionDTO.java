package servlet.pojo;

import servlet.util.ServletUtils;

import javax.transaction.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDTO {
    String type;
    String date;
    int amount;
    int balanceBefore;
    int balanceAfter;

    public TransactionDTO(Transaction transaction) {
        this.type = transaction.getType();
        this.date = ServletUtils.formatDateToString(transaction.getDate());
        this.amount = transaction.getAmount();
        this.balanceBefore = transaction.getBalanceBefore();
        this.balanceAfter = transaction.getBalanceAfter();
    }
}
