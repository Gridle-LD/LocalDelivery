package com.example.localdelivery.model;

public class FeedbackData {

    private int rating;
    private String feedback;

    public FeedbackData(int rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
