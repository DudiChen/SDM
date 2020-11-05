package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.Customer;
import servlet.pojo.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "UsersServlet", urlPatterns = {"/api/users"})
public class UsersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject reply = new JsonObject();
        List<Customer> customers = Controller.getInstance().getAllCustomers();
        List<UserDTO> userDTOs = customers.stream().map(UserDTO::new).collect(Collectors.toList());
        Gson gson = new Gson();
        JsonArray usersJSON = gson.toJsonTree(userDTOs).getAsJsonArray();
        reply.add("allUsers", usersJSON);
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }
}
