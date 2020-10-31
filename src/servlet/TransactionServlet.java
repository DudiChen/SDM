package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import servlet.pojo.TransactionDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/api/users/transactions"})
public class TransactionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        double amount = body.get("amount").getAsDouble();
        String dateString = body.get("date").getAsString();
        Date date = ServletUtils.formatStringToDate(dateString);
        // TODO: Consumer recharge; add credit to consumer account
        // Dummy:
        response.getWriter().write("Great Success!");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        // TODO: fetch transaction history by uuid
        // type {recharge, payment, incoming}
        // date  , amount, balance-before, balance-after
        // Dummy:
        String reply = "";
        if (uuid.equals("111")) {
            TransactionDTO transaction1 = new TransactionDTO("income", new Date(), 2, 1, 3);
            TransactionDTO transaction2 = new TransactionDTO("payment", new Date(), 2, 3, 1);
            List<TransactionDTO> transactionList = Arrays.asList(transaction1, transaction2);
            Gson gson = new Gson();
//        Type transactionListType = new TypeToken<ArrayList<Transaction>>(){}.getType();
            JsonElement temp = gson.toJsonTree(transactionList);
            JsonArray transactionListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("transactions", transactionListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: user not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
