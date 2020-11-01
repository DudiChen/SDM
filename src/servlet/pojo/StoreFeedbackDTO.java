package servlet.pojo;

import entity.Feedback;

public class StoreFeedbackDTO {
    String customerName;
    String text;
    int rating;

    public StoreFeedbackDTO(Feedback feedback) {
        this.customerName = feedback.getCustomerName();
        this.text = feedback.getText();
        this.rating = feedback.getRating();
    }
}
