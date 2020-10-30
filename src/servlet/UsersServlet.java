package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "UsersServlet", urlPatterns = {"/api/users"})
public class UsersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject reply = new JsonObject();
        // TODO: fetch all users list
        // Dummy:
        Gson gson = new Gson();
        List<String> areasList = Arrays.asList("dudi", "noam");
        JsonArray usersJSON = gson.toJsonTree(areasList).getAsJsonArray();
        reply.add("allUsers", usersJSON);
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }
}
