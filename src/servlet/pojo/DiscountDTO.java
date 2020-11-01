package servlet.pojo;

import entity.Discount;
import servlet.util.ServletUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountDTO {
    String name;
    String type;
    List<OfferDTO> offers;
    double quantity;

    public DiscountDTO(Discount discount) {
        this.name = discount.getName();
        this.type = ServletUtils.parseDiscountOperator(discount.getOperator());
        this.offers = discount.getOffers().stream().map(OfferDTO::new).collect(Collectors.toList());
        this.quantity = discount.getQuantity();
    }
}
