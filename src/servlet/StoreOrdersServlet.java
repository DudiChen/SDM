package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.StoreDTO;
import servlet.pojo.StoreOrderDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "StoreOrdersServlet", urlPatterns = {"/api/areas/stores/orders"})
public class StoreOrdersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        String storeId = request.getParameter("storeId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        JsonObject orderJSON = body.get("order").getAsJsonObject();
        JsonObject discountsJSON = body.get("discounts").getAsJsonObject();
        Gson gson = new Gson();
        StoreOrderDTO order = gson.fromJson(orderJSON, StoreOrderDTO.class);
        // TODO: add validation check for the StoreOrder created
        boolean isValid = true;
        JsonObject replyJSON = new JsonObject();
        replyJSON.addProperty("isValid", isValid);
        String reply = String.valueOf(replyJSON.getAsJsonObject());
        response.getWriter().write(reply);
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
