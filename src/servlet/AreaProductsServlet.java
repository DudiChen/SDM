package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.Product;
import servlet.pojo.ProductInAreaDTO;
import servlet.pojo.StoreDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AreaProductsServlet", urlPatterns = {"/api/areas/products"})
public class AreaProductsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        List<Product> productList = Controller.getInstance().getAllProductInArea(areaId);
        List<ProductInAreaDTO> productDTOsList = productList.stream().map(ProductInAreaDTO::new).collect(Collectors.toList());
        // TODO: fetch all products in area according to areaId
        // Dummy:
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(productDTOsList);
        JsonArray productsListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allProducts", productsListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
