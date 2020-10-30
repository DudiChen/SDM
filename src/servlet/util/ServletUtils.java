package servlet.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {
    public static JsonObject readRequestBodyAsJSON(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        StringBuilder bodyBuilder = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            bodyBuilder.append(line);
        }
        JsonElement je = new JsonParser().parse(bodyBuilder.toString());
        JsonObject jsonResult = je.getAsJsonObject();
        return jsonResult;
    }
}
