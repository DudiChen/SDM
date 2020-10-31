package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.Controller;
import servlet.pojo.StoreFeedbackDTO;
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

@WebServlet(name = "StoreFeedbacksServlet", urlPatterns = {"/api/areas/stores/feedbacks"})
public class StoreFeedbacksServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
//        String uuid = request.getParameter("uuid");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        String uuid = body.get("uuid").getAsString();
        int rating = body.get("rating").getAsInt();
        String text = body.get("text").getAsString();
        Controller.getInstance().addStoreFeedback(uuid, areaId, storeId, rating, text);
        // String customerName = <function that gets customer name by uuid>
//        StoreFeedbackDTO storeRating = new StoreFeedbackDTO(customerName, rating, text);
        response.getWriter().write("Great Success");
        response.getWriter().close();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
//        String areaId = request.getParameter("areaId");
//        String storeId = request.getParameter("storeId");
        String areaId = body.get("areaId").getAsString();
        String storeId = body.get("storeId").getAsString();
        List<Feedback> feedbacks = Controller.getInstance().getStoreFeedbacks(areaId, storeId);
        List<StoreFeedbackDTO> storeFeedbackDTOs = feedbacks.stream().map(StoreFeedbackDTO::new).collect(Collectors.toList());
        String reply = "";
        Gson gson = new Gson();
        JsonElement temp = gson.toJsonTree(storeFeedbackDTOs);
        JsonArray feedbackListJSON = temp.getAsJsonArray();
        JsonObject replyJSON = new JsonObject();
        replyJSON.add("allFeedbacks", feedbackListJSON);
        reply = String.valueOf(replyJSON);
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
