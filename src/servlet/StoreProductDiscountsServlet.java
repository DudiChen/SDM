package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.Discount;
import servlet.pojo.DiscountDTO;
import servlet.pojo.OfferDTO;
import servlet.pojo.ProductInStoreDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "StoreProductDiscountsServlet", urlPatterns = {"/api/areas/stores/products/discounts"})
public class StoreProductDiscountsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
//        String productId = request.getParameter("productId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        int areaId = body.get("areaId").getAsInt();
        int storeId = body.get("storeId").getAsInt();
        String productId = body.get("productId").getAsString();
        List<Discount> discountList = Controller.getInstance().getDiscountsInStoreByProductId(areaId
                , storeId, Integer.parseInt(productId));
        List<DiscountDTO> discountDTOS = discountList.stream().map(discount -> new DiscountDTO(discount, areaId)).collect(Collectors.toList());
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(discountDTOS);
        JsonArray productsListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allDiscounts", productsListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
