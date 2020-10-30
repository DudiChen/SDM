package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
        String uuid = request.getParameter("uuid");
        // TODO: fetch balance according to uuid
        // Dummy:
        String reply = "";
        if (uuid.equals("111")) {
            JsonObject replyJSON = new JsonObject();
            Gson gson = new Gson();
            replyJSON.addProperty("balance", 750);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: User was not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
