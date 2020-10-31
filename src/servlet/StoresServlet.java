package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import entity.Store;
import servlet.pojo.StoreDTO;
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

@WebServlet(name = "StoresServlet", urlPatterns = {"/api/areas/stores"})
public class StoresServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String ownerName = body.get("ownerName").getAsString();
        String storeName = body.get("storeName").getAsString();
        int x = body.get("storeX").getAsInt();
        int y = body.get("storeY").getAsInt();
        double ppk = body.get("ppk").getAsDouble();
        int soldProducts = body.get("soldProducts").getAsInt();
        // TODO: add store to the system
        // Dummy:
        response.getWriter().write("Great Success!");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        List<Store> stores = Controller.getInstance().getAllStoresInArea(areaId);
        List<StoreDTO> storeDTOs = stores.stream().map(StoreDTO::new).collect(Collectors.toList());
        // Dummy:
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(storeDTOs);
        JsonArray storesListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allStores", storesListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
