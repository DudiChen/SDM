package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import servlet.util.*;

// TODO: Consider changing url to /api/user and use it both for login(GET) and signup(POST)
@WebServlet(name = "SignupServlet", urlPatterns = {"/api/signup"})
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String username = body.get("username").getAsString();
        String password = body.get("password").getAsString();
        String role = body.get("role").getAsString();
        // TODO: parse to userDTO (via Gson) & add signed up user
//        JsonObject reply = new JsonObject();
//        reply.addProperty("role", role);
//        response.getWriter().write(reply.toString());
//        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
