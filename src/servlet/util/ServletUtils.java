package servlet.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String formatDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static Date formatStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {}
        return date;
    }
}
