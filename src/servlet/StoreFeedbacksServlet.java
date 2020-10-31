package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

@WebServlet(name = "StoreFeedbacksServlet", urlPatterns = {"/api/areas/stores/feedbacks"})
public class StoreFeedbacksServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        String storeId = request.getParameter("storeId");
        String uuid = request.getParameter("uuid");
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        int rating = body.get("rating").getAsInt();
        String text = body.get("text").getAsString();
        // String customerName = <function that gets customer name by uuid>
//        StoreFeedbackDTO storeRating = new StoreFeedbackDTO(customerName, rating, text);
        // TODO: add rating to store in the system
        // Dummy:
        response.getWriter().write("Great Success");
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaId = request.getParameter("areaId");
        String storeId = request.getParameter("storeId");

        // TODO: fetch all stores in area according to areaId
        // Dummy:
        String reply = "";
        if (areaId.equals("1") && storeId.equals("1")) {
            StoreFeedbackDTO storeFeedback1 = new StoreFeedbackDTO("Menashe", "Wallak sababa", 4);
            StoreFeedbackDTO storeFeedback2 = new StoreFeedbackDTO("Shirli", "kaka", 2);
            List<StoreFeedbackDTO> feedbacksList = Arrays.asList(storeFeedback1, storeFeedback2);
            Gson gson = new Gson();
            JsonElement temp = gson.toJsonTree(feedbacksList);
            JsonArray feedbackListJSON = temp.getAsJsonArray();
            JsonObject replyJSON = new JsonObject();
            replyJSON.add("allFeedbacks", feedbackListJSON);
            reply = String.valueOf(replyJSON);
        }
        else {
            reply = "ERROR: area not found!";
        }
        response.getWriter().write(reply);
        response.getWriter().close();
    }
}
