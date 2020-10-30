package servlet.pojo;

import java.util.List;

public class DiscountDTO {
    String name;
    String type;
    List<OfferDTO> offers;
    double quantity;

    public DiscountDTO(String name, String type, List<OfferDTO> offers, double quantity) {
        this.name = name;
        this.type = type;
        this.offers = offers;
        this.quantity = quantity;
    }
}
