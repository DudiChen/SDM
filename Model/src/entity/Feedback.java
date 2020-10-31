package entity;

public class Feedback {
    private String customerName;
    private int rating;
    private String text;

    public Feedback(String customerName, int rating, String text) {
        this.customerName = customerName;
        this.rating = rating;
        this.text = text;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
