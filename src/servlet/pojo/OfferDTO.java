package servlet.pojo;

public class OfferDTO {
    String productName;
    double additionalCost;
    double quantity;

    public OfferDTO(String productName, double additionalCost, double quantity) {
        this.productName = productName;
        this.additionalCost = additionalCost;
        this.quantity = quantity;
    }
}
