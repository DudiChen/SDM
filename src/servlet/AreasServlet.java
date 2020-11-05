package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.Controller;
import servlet.pojo.AreaDTO;
import servlet.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AreasServlet", urlPatterns = {"/api/areas"})
public class AreasServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject body = ServletUtils.readRequestBodyAsJSON(request);
        String uuid = body.get("uuid").getAsString();
        String xmlFileBase64 = body.get("xmlFile").getAsString();
        String result = Controller.getInstance().loadXMLData(ServletUtils.generateFileFromBase64(xmlFileBase64), Integer.parseInt(uuid));
        JsonObject reply = new JsonObject();
        reply.addProperty("message", result);
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject reply = new JsonObject();
        Gson gson = new Gson();
        List<AreaDTO> areasList = Controller.getInstance().getAllAreas().stream()
                .map(AreaDTO::new)
                .collect(Collectors.toList());
        JsonArray areasJSON = gson.toJsonTree(areasList).getAsJsonArray();
        reply.add("allAreas", areasJSON);
        response.getWriter().write(String.valueOf(reply));
        response.getWriter().close();
    }


}
