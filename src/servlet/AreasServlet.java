package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.AreaDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AreasServlet", urlPatterns = {"/api/areas"})
public class AreasServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject reply = new JsonObject();
        // TODO: fetch all areas list
        // Dummy:
        Gson gson = new Gson();

        List<AreaDTO> areasList = Arrays.asList(
                new AreaDTO("Shenkin", "Noam", 30, 3, 300, 150.0),
                new AreaDTO("Dizingoff", "Dudi", 10, 1, 100, 40.0)
        );
        JsonArray areasJSON = gson.toJsonTree(areasList).getAsJsonArray();
        reply.add("allAreas", areasJSON);
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }
}
