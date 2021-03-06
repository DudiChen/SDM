package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.Controller;
import entity.Customer;
import entity.Store;
import servlet.pojo.ProductInNewStoreDTO;
import servlet.pojo.StoreDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "StoresServlet", urlPatterns = {"/api/areas/stores"})
public class StoresServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String uuid = body.get("uuid").getAsString();
        String storeName = body.get("storeName").getAsString();
        int x = body.get("storeX").getAsInt();
        int y = body.get("storeY").getAsInt();
        double ppk = body.get("ppk").getAsDouble();
        Gson gson = new Gson();
        // id, price
        List<ProductInNewStoreDTO> productInNewStoreDTOs = gson.fromJson(body.get("soldProducts").getAsJsonArray(), new TypeToken<ArrayList<ProductInNewStoreDTO>>(){}.getType());
        Map<String, Integer> productIdToPriceInNewStore = new HashMap<>();
        for(ProductInNewStoreDTO dto : productInNewStoreDTOs) {
            productIdToPriceInNewStore.put(dto.getId(), dto.getPrice());
        }
        Controller.getInstance().addNewStoreToArea(Integer.parseInt(uuid), Integer.parseInt(areaId), storeName, new Point(x,y), productIdToPriceInNewStore, ppk);
        response.getWriter().write("Great Success!");
        response.getWriter().close();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String uuid = body.get("uuid").getAsString();
        List<Store> stores = Controller.getInstance().getAllStoresInArea(Integer.parseInt(areaId));
        Customer customer = Controller.getInstance().getCustomerById(Integer.parseInt(uuid));
        if(customer.getRole().equals(Customer.Role.SELLER)) {
            stores = stores.stream().filter(store -> store.getOwnerName().equals(customer.getName())).collect(Collectors.toList());
        }
        List<StoreDTO> storeDTOs = stores.stream().map(StoreDTO::new).collect(Collectors.toList());
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
