package servlet;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import controller.Controller;
import entity.Customer;
import servlet.util.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/api/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String userName = body.get("userName").getAsString();
        JsonObject reply = new JsonObject();
        Customer customer = Controller.getInstance().getCustomerByName(userName);
        if (customer == null) {
            reply.addProperty("errorMessage", "user not found");
        } else {

            String ssid = request.getSession().getId();
            reply.addProperty("role", customer.getRole().getName().toLowerCase());
            reply.addProperty("ssid", ssid);
            reply.addProperty("uuid", Integer.toString(customer.getId()));
        }
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
