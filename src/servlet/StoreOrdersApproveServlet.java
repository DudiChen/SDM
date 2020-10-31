package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import servlet.pojo.StoreDTO;
import servlet.pojo.StoreOrderDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "StoreOrdersApproveServlet", urlPatterns = {"/api/areas/stores/orders/approve"})
public class StoreOrdersApproveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        String storeId = request.getParameter("storeId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        JsonObject orderJSON = body.get("order").getAsJsonObject();
        JsonObject discountsJSON = body.get("discounts").getAsJsonObject();
        Gson gson = new Gson();
        StoreOrderDTO order = gson.fromJson(orderJSON, StoreOrderDTO.class);
        // TODO: add order and discounts to system
//        boolean isValid = true;
//        JsonObject replyJSON = new JsonObject();
//        replyJSON.addProperty("isValid", isValid);
//        String reply = String.valueOf(replyJSON.getAsJsonObject());
        response.getWriter().write("Great Success");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
