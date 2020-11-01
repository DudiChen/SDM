package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.InvoiceDiscountProduct;
import entity.InvoiceProduct;
import entity.StoreProduct;
import servlet.pojo.ProductInOrderDetailsDTO;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@WebServlet(name = "StoreOrderProductsServlet", urlPatterns = {"/api/areas/stores/orders/products"})
public class StoreOrderProductsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        String orderId = body.get("orderId").getAsString();
        String uuid = body.get("uuid").getAsString();

        List<InvoiceProduct> storeProducts = Controller.getInstance()
                .getAllProductsInStoreOrder(Integer.parseInt(areaId), Integer.parseInt(storeId), Integer.parseInt(orderId));
        List<ProductInOrderDetailsDTO> productInStoreDTOs = storeProducts.stream().map(ProductInOrderDetailsDTO::new).collect(Collectors.toList());
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
//        String orderId = request.getParameter("orderId");
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(productInStoreDTOs);
        JsonArray productsListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allProducts", productsListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
