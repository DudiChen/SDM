package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.Controller;
import jdk.nashorn.internal.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import servlet.util.*;

@WebServlet(name = "SignupServlet", urlPatterns = {"/api/signup"})
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String username = body.get("userName").getAsString();
        String role = body.get("role").getAsString();
        JsonObject reply = new JsonObject();
        if(role == null || role.isEmpty()) {
            role = "consumer";
        }
        // TODO: add check for already available customer under the same name
        boolean isSuccessfullyAdded = Controller.getInstance().addCustomer(username, role);
        if(!isSuccessfullyAdded) {
            reply.addProperty("errorMessage", "error registering user");
        }
        response.getWriter().write(reply.toString());
        response.getWriter().close();
    }
}
