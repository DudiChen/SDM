package servlet.pojo;

import controller.Controller;
import entity.Discount;

public class OfferDTO {
    String productName;
    double additionalCost;
    double quantity;

    public OfferDTO(Discount.Offer offer) {
        this.productName = Controller.getInstance().getProductById(offer.getProductId()).getName();
        this.additionalCost = offer.getForAdditional();
        this.quantity = offer.getQuantity();
    }
}
