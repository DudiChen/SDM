package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.Discount;
import entity.OrderInvoice;
import entity.StoreProduct;
import servlet.pojo.DiscountDTO;
import servlet.pojo.ProductInOrderDTO;
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
        Type productsMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Gson gson = new Gson();
        // maps can be empty
        // in the first call of a consumer it will defenatly be empty
        Map<String, List<Integer>> discountNameToProductIdInOffer = gson.fromJson(body.get("discounts").getAsJsonObject(), discountsMapType);
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsJsonObject(), productsMapType);
        Map<Integer, Double> productIdToQuantity2 = ServletUtils.productIdToQuantityWithGramsConsiderationAndStringForIdConsideration(areaId, productIdToQuantity);
        Controller controller = Controller.getInstance();
        List<ProductInOrderDTO> storeProductsSTOs = productIdToQuantity2.entrySet().stream()
                .map(entry -> {
                    final StoreProduct storeProduct = controller.getStoreProductById(Integer.parseInt(areaId), Integer.parseInt(storeId), entry.getKey());
                    return new ProductInOrderDTO(
                            Integer.toString(storeProduct.getId()),
                            storeProduct.getName(),
                            storeProduct.getPurchaseMethod().toString(),
                            entry.getValue(),
                            storeProduct.getPrice(),
                            entry.getValue() * storeProduct.getPrice()
                    );
                }).collect(Collectors.toList());
        List<Discount> availableDiscounts = controller.getAvailableDiscounts(Integer.parseInt(areaId), Integer.parseInt(storeId), discountNameToProductIdInOffer, productIdToQuantity2);
        JsonObject replyJSON = new JsonObject();
        boolean isValid = true;
        if (availableDiscounts == null) {
            isValid = false;
        } else {
            List<DiscountDTO> availableDiscountDTOs = availableDiscounts.stream().map(discount -> new DiscountDTO(discount, Integer.parseInt(areaId))).collect(Collectors.toList());
            String availableDiscountsStr = gson.toJson(availableDiscountDTOs);
            replyJSON.addProperty("discounts", availableDiscountsStr);
        }
        JsonElement productsJSON = gson.toJsonTree(storeProductsSTOs);
        replyJSON.add("products", productsJSON);
        replyJSON.addProperty("isValid", Boolean.toString(isValid));
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
