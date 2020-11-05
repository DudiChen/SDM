package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.Discount;
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
        int areaId = body.get("areaId").getAsInt();
        int storeId = body.get("storeId").getAsInt();
        int uuid = body.get("uuid").getAsInt();
        // TODO: NOAM: Add date from UI
        String dateString = body.get("date").getAsString();
        Date date = ServletUtils.formatStringToDate(dateString);

        Gson gson = new Gson();
        Type discountsMapType = new TypeToken<HashMap<String, ArrayList<Integer>>>() {
        }.getType();
        Type productsMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Map<String, List<Integer>> discountNameToProductIdInOffer = gson.fromJson(body.get("discounts").getAsString(), discountsMapType);
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsString(), productsMapType);
        // TODO: next method should perform notifications and
        Controller.getInstance().performOrderForStore(uuid, areaId, storeId, date, discountNameToProductIdInOffer, productIdToQuantity);
        response.getWriter().write("Great Success");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
