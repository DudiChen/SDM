package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BalanceServlet", urlPatterns = {"/api/balance"})
public class BalanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uuid = Integer.parseInt(request.getParameter("uuid"));
        // Dummy:
        String reply = "";
        JsonObject replyJSON = new JsonObject();
        Gson gson = new Gson();
        replyJSON.addProperty("balance", Controller.getInstance().getBalanceByCustomerId(uuid));
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
