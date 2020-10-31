package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        String productId = body.get("productId").getAsString();
        // TODO: fetch all discounts according to product in store of area according to areaId, storeId & productId
        // Dummy:
        String reply = "";
        if (areaId.equals("1")) {
            DiscountDTO discount1 = new DiscountDTO("Baal Abait Eshtagea",
                    "all-or-nothing",
                    Arrays.asList(
                            new OfferDTO("Banana", 0.0, 3.0),
                            new OfferDTO("Nachle-Coffe", 2.0, 1.0)
                    ), 7);
            List<DiscountDTO> discountsList = Arrays.asList(discount1);
            Gson gson = new Gson();
            JsonElement temp = gson.toJsonTree(discountsList);
            JsonArray productsListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("allDiscounts", productsListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: area not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
