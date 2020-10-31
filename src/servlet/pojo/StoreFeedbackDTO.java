package servlet.pojo;

public class StoreFeedbackDTO {
    String customerName;
    String text;
    int rating;

    public StoreFeedbackDTO(String customerName, String text, int rating) {
        this.customerName = customerName;
        this.text = text;
        this.rating = rating;
    }
}
