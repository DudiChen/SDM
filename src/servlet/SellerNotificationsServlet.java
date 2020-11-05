package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SellerNotificationsServlet", urlPatterns = {"api/users/notifications"})
public class SellerNotificationsServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uuid = Integer.parseInt(request.getParameter("uuid"));
        List<String> allNotifications = Controller.getInstance().getSellerNotifications(uuid);
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(allNotifications);
        JsonArray storesListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allNotifications", storesListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
