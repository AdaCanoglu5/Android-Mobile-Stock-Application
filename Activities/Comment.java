package com.sutock2;

public class Comment {

    private String id;
    private String text;
    private User user;
    private Stock stock;

    // Constructors, getters, and setters

    public Comment() {
        // Default constructor
    }

    public Comment(String text, User user, Stock stock) {
        this.text = text;
        this.user = user;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}