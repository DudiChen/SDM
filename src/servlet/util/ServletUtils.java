package servlet.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Discount;
import controller.Controller;
import entity.Product;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public static File generateFileFromBase64(final String base64) {
        byte[] byteFile = DatatypeConverter.parseBase64Binary(base64);
//        try (FileOutputStream fos = new FileOutputStream("area.xml")) {
//            fos.write(byteFile);
//        } catch (IOException e) {
//            // TODO: Decide what to do in case of File IO related exception
//        }
        File xmlFile = null;
        try {
            xmlFile = File.createTempFile("areaFile_" + Math.abs(new Random().nextInt() * Integer.MAX_VALUE), ".xml", null);
            xmlFile.deleteOnExit();

        } catch (IOException e) {
            // TODO: Decide what to do with File IO related exception
        }
        try (FileOutputStream fos = new FileOutputStream(xmlFile.getAbsoluteFile())) {
            fos.write(byteFile);
        } catch (IOException e) {
            // TODO: Decide what to do in case of File IO related exception
        }

        return xmlFile;
//        return new File(ServletUtils.class.getProtectionDomain().getCodeSource().getLocation() + "/area.xml");
    }

    public static String parseDiscountOperator(Discount.DiscountOperator operator) {
        String withoutMakaf = operator.toString().replace('-', ' ').toLowerCase();
        return Character.toUpperCase(withoutMakaf.charAt(0)) + withoutMakaf.substring(1);
    }

    public static Map<Integer, Double> productIdToQuantityWithGramsConsiderationAndStringForIdConsideration(String areaId, Map<String, Integer> productIdToQuantity) {
        Map<Integer, Double> productIdToQuantity2 = new HashMap<>();
        for(Map.Entry<String, Integer> pair: productIdToQuantity.entrySet()) {
            double quantity = pair.getValue();
            Product product = Controller.getInstance().getAreaProductById(Integer.parseInt(areaId), Integer.parseInt(pair.getKey()));
            if (product.getPurchaseMethod() == Product.PurchaseMethod.WEIGHT) {
                // by grams
                quantity = pair.getValue() / 1000;
            }
            productIdToQuantity2.put(product.getId(), quantity);
        }
        return productIdToQuantity2;
    }
}
