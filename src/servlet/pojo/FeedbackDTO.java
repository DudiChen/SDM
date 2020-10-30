package servlet.pojo;

public class FeedbackDTO {
    String customerName;
    String text;
    int rating;

    public FeedbackDTO(String customerName, String text, int rating) {
        this.customerName = customerName;
        this.text = text;
        this.rating = rating;
    }
}
