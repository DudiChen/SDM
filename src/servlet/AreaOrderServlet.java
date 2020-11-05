package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AreaOrderServlet", urlPatterns = {"api/areas/orders/approved"})
public class AreaOrderServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);

        String areaId = body.get("areaId").getAsString();
        String uuid = body.get("uuid").getAsString();

        Gson gson = new Gson();
        Type productsMapType = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Map<String, Integer> productIdToQuantity = gson.fromJson(body.get("order").getAsString(), productsMapType);

        boolean isSuccessful = Controller.getInstance().orderFromArea(uuid, areaId, productIdToQuantity);
        if(isSuccessful){
            response.getWriter().write("Great Success");
        }
        else {
            response.getWriter().write("Error");
        }
        response.getWriter().close();
    }
}
