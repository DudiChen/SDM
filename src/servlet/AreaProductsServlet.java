package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

@WebServlet(name = "AreaProductsServlet", urlPatterns = {"/api/areas/products"})
public class AreaProductsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        // TODO: fetch all products in area according to areaId
        // Dummy:
        String reply = "";
        if (areaId.equals("1")) {
            ProductInAreaDTO product1 = new ProductInAreaDTO("Banana", "1", "Weight", 2, 3.0, 10);
            ProductInAreaDTO product2 = new ProductInAreaDTO("Nachle-Coffee", "2", "Quantity", 1, 12.0, 7);
            List<ProductInAreaDTO> productsList = Arrays.asList(product1, product2);
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
