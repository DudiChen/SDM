package servlet.pojo;

public class OfferDTO {
    String productName;
    int additionalCost;
    String quantity;

    public OfferDTO(String productName, int additionalCost, String quantity) {
        this.productName = productName;
        this.additionalCost = additionalCost;
        this.quantity = quantity;
    }
}
