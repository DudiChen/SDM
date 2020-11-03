package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import servlet.pojo.TransactionDTO;
import servlet.util.ServletUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transaction;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/api/users/transactions"})
public class TransactionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        double amount = body.get("amount").getAsDouble();
        String dateString = body.get("date").getAsString();
        Date date = ServletUtils.formatStringToDate(dateString);
        Controller.getInstance().rechargeCustomerBalance(Integer.parseInt(uuid), amount, date);
        response.getWriter().write("Great Success!");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        List<entity.Transaction> transactions = Controller.getInstance().getTransactionsForCustomer(Integer.parseInt(uuid));
        List<TransactionDTO> transactionDTOs = transactions.stream().map(TransactionDTO::new).collect(Collectors.toList());
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(transactionDTOs);
        JsonArray transactionListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("transactions", transactionListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
