package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.ProductInAreaDTO;
import servlet.pojo.ProductInStoreDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "StoreProductsServlet", urlPatterns = {"/api/areas/stores/products"})
public class StoreProductsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        String storeId = request.getParameter("storeId");
        // TODO: fetch all products in store of area according to areaId & storeId
        // Dummy:
        String reply = "";
        if (areaId.equals("1")) {
            ProductInStoreDTO product1 = new ProductInStoreDTO("Nachle-Coffe", "2","Weight", 12.0 );
            List<ProductInStoreDTO> productsList = Arrays.asList(product1);
            Gson gson = new Gson();
            JsonElement temp = gson.toJsonTree(productsList);
            JsonArray productsListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("allProducts", productsListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: area not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
