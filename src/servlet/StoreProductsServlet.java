package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.Product;
import entity.StoreProduct;
import servlet.pojo.ProductInAreaDTO;
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

@WebServlet(name = "StoreProductsServlet", urlPatterns = {"/api/areas/stores/products"})
public class StoreProductsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        List<StoreProduct> productList = Controller.getInstance().getAllProductsInStore(Integer.parseInt(areaId), Integer.parseInt(storeId));
        List<ProductInStoreDTO> productInStoreDTOS = productList.stream().map(ProductInStoreDTO::new).collect(Collectors.toList());
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(productInStoreDTOS);
        JsonArray productsListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allProducts", productsListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
