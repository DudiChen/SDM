package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.StoreDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "StoresServlet", urlPatterns = {"/api/areas/stores"})
public class StoresServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String ownerName = body.get("ownerName").getAsString();
        String storeName = body.get("storeName").getAsString();
        int x = body.get("storeX").getAsInt();
        int y = body.get("storeY").getAsInt();
        double ppk = body.get("ppk").getAsDouble();
        int soldProducts = body.get("soldProducts").getAsInt();
        // TODO: add store to the system
        // Dummy:
        response.getWriter().write("Great Success!");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        // TODO: fetch all stores in area according to areaId
        // Dummy:
        String reply = "";
        if (areaId.equals("1")) {
            StoreDTO store1 = new StoreDTO("Rami", "1", "Reoma", 20, 2000.0, 20.0,5);
            StoreDTO store2 = new StoreDTO("Sefa", "2", "Ramzi", 25, 2500.0, 25.0,10);
            List<StoreDTO> storesList = Arrays.asList(store1, store2);
            Gson gson = new Gson();
            JsonElement temp = gson.toJsonTree(storesList);
            JsonArray storesListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("allStores", storesListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: area not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
