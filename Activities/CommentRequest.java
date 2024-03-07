package com.sutock2;

import com.google.gson.annotations.SerializedName;

public class CommentRequest {
    @SerializedName("text")
    private String text;
    @SerializedName("userId")
    private String userId;
    @SerializedName("stockId")
    private String stockId;

    // Constructors, getters, and setters

    public CommentRequest() {
    }

    public CommentRequest(String text, String userId, String stockId) {
        this.text = text;
        this.userId = userId;
        this.stockId = stockId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

}
