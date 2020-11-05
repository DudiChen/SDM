package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.Discount;
import exception.OrderValidationException;
import servlet.pojo.StoreDTO;
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

@WebServlet(name = "StoreOrdersApproveServlet", urlPatterns = {"/api/areas/stores/orders/approve"})
public class StoreOrdersApproveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        String uuid = body.get("uuid").getAsString();
        String dateString = body.get("date").getAsString();
        Date date = ServletUtils.formatStringToDate(dateString);
        Gson gson = new Gson();
        Type discountsMapType = new TypeToken<HashMap<String, ArrayList<Integer>>>() {
        }.getType();
        Type productsMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Map<String, List<Integer>> discountNameToProductIdInOffer = gson.fromJson(body.get("discounts").getAsJsonObject(), discountsMapType);
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsJsonObject(), productsMapType);
        // TODO: next method should perform notifications and
        Map<Integer, Double> productIdToQuantity2 = ServletUtils.productIdToQuantityWithGramsConsiderationAndStringForIdConsideration(areaId, productIdToQuantity);
        int orderId = 0;
        String errorMessage = "";
        try {
            orderId = Controller.getInstance().performOrderForStore(Integer.parseInt(uuid), Integer.parseInt(areaId), Integer.parseInt(storeId), date, discountNameToProductIdInOffer, productIdToQuantity2);
        } catch (OrderValidationException e) {
            errorMessage = e.getMessage();
        }
        String reply = "";
        JsonObject replyJSON = new JsonObject();
        replyJSON.addProperty("orderId", Integer.toString(orderId));
        replyJSON.addProperty("errorMessage", errorMessage);
        reply = String.valueOf(replyJSON);
        response.getWriter().write("Great Success");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
