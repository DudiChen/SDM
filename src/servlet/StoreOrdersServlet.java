package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.Discount;
import entity.OrderInvoice;
import servlet.pojo.DiscountDTO;
import servlet.pojo.StoreOrderDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

// get available discounts from products and discounts map !
@WebServlet(name = "StoreOrdersServlet", urlPatterns = {"/api/areas/stores/orders"})
public class StoreOrdersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();

        Type discountsMapType = new TypeToken<HashMap<String, ArrayList<Integer>>>() {
        }.getType();
        Type productsMapType = new TypeToken<HashMap<String, Integer>() {
        }.getType();
        Gson gson = new Gson();
        // maps can be empty
        // in the first call of a consumer it will defenatly be empty
        Map<String, List<Integer>> discountNameToProductIdInOffer = gson.fromJson(body.get("discounts").getAsString(), discountsMapType);
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsString(), productsMapType);
        List<Discount> availableDiscounts = Controller.getInstance().getAvailableDiscounts(areaId, storeId, discountNameToProductIdInOffer, productIdToQuantity);
        JsonObject replyJSON = new JsonObject();
        boolean isValid = true;
        if (availableDiscounts == null) {
            isValid = false;
        } else {
            List<DiscountDTO> availableDiscountDTOs = availableDiscounts.stream().map(DiscountDTO::new).collect(Collectors.toList());
            String availableDiscountsStr = gson.toJson(availableDiscountDTOs);
            replyJSON.addProperty("discounts", availableDiscountsStr);
        }
        replyJSON.addProperty("isValid", isValid);
        String reply = String.valueOf(replyJSON.getAsJsonObject());
        response.getWriter().write(reply);
        response.getWriter().close();
    }

    // get all orders of store for seller
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();

        List<OrderInvoice> orderInvoices = Controller.getInstance().getAllOrdersForStore(Integer.parseInt(areaId), Integer.parseInt(storeId));
        List<StoreOrderDTO> storeOrderDTOs = orderInvoices.stream().map(StoreOrderDTO::new).collect(Collectors.toList());
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(storeOrderDTOs);
        JsonArray ordersListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allOrders", ordersListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
