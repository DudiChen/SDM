package servlet.pojo;

import servlet.util.ServletUtils;

import javax.transaction.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDTO {
    String type;
    String date;
    double amount;
    double balanceBefore;
    double balanceAfter;

    public TransactionDTO(entity.Transaction transaction) {
        this.type = transaction.getTransactionType().name().toLowerCase();
        this.date = ServletUtils.formatDateToString(transaction.getDate());
        this.amount = transaction.getAmount();
        this.balanceBefore = transaction.getPreviousBalance();
        this.balanceAfter = this.balanceBefore + transaction.getAmount();
    }
}
