package servlet.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Discount;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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

    public static File saveFIle(final String base64) {
        byte[] byteFile = DatatypeConverter.parseBase64Binary(base64);
        return new File(ServletUtils.class.getProtectionDomain().getCodeSource().getLocation() + "/area.xml");
    }

    public static String parseDiscountOperator(Discount.DiscountOperator operator) {
        String withoutMakaf = operator.toString().replace('-', ' ').toLowerCase();
        return Character.toUpperCase(withoutMakaf.charAt(0)) + withoutMakaf.substring(1);
    }
}
