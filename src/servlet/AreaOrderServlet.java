package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.OrderInvoice;
import entity.Product;
import exception.OrderValidationException;
import servlet.pojo.AreaOrderDTO;
import servlet.pojo.StoreOrderDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "AreaOrderServlet", urlPatterns = {"/api/areas/orders/approved"})
public class AreaOrderServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);

        String areaId = body.get("areaId").getAsString();
        String uuid = body.get("uuid").getAsString();
        String dateString = body.get("date").getAsString();
        Date date = ServletUtils.formatStringToDate(dateString);

        Gson gson = new Gson();
        Type productsMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsJsonObject(), productsMapType);
        Map<Integer, Double> productIdToQuantity2 = ServletUtils.productIdToQuantityWithGramsConsiderationAndStringForIdConsideration(areaId, productIdToQuantity);

        String reply = "";
        JsonObject replyJSON = new JsonObject();

        try {
            List<Integer> orderIds = Controller.getInstance().orderFromArea(Integer.parseInt(uuid), Integer.parseInt(areaId), date, productIdToQuantity2);
            List<OrderInvoice> orders = orderIds.stream().map(orderId -> Controller.getInstance().getAreaById(Integer.parseInt(areaId)).getOrderInvoice(orderId)).collect(Collectors.toList());
            List<AreaOrderDTO> orderDTOs = orders.stream().map(orderInvoice -> new AreaOrderDTO(orderInvoice, Controller.getInstance().getAreaById(Integer.parseInt(areaId)))).collect(Collectors.toList());

            JsonElement temp = gson.toJsonTree(orderDTOs);
            JsonArray ordersListJSON = temp.getAsJsonArray();
            replyJSON.add("allOrders", ordersListJSON);
        } catch (OrderValidationException e) {
            String errorMessage = e.getMessage();
            replyJSON.addProperty("errorMessage", errorMessage);
        } finally {
            reply = String.valueOf(replyJSON);
            response.getWriter().write(reply);
            response.getWriter().close();
        }
    }
}
