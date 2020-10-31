package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.ProductInAreaDTO;
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
import java.util.Date;
import java.util.List;

@WebServlet(name = "StoreOrdersServlet", urlPatterns = {"/api/areas/stores/orders"})
public class StoreOrdersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
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

    //     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        // TODO: fetch all orders of store in area according to areaId & store
        // Dummy:
        String reply = "";
        if (areaId.equals("111")) {
            StoreOrderDTO order1 = new StoreOrderDTO("Yemima", "1", new Date(), 15, 200.0, 7.0);
            StoreOrderDTO order2 = new StoreOrderDTO("Charlie", "1", new Date(), 22, 222.0, 2.0);
            List<StoreOrderDTO> ordersList = Arrays.asList(order1, order2);
            Gson gson = new Gson();
            JsonElement temp = gson.toJsonTree(ordersList);
            JsonArray ordersListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("allOrders", ordersListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: area not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
