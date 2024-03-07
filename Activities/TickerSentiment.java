package com.sutock2;

public class TickerSentiment {

    private String ticker;
    private String relevance_score;
    private String ticker_sentiment_score;
    private String ticker_sentiment_label;

    public TickerSentiment(String ticker, String relevance_score,
                           String ticker_sentiment_score, String ticker_sentiment_label) {
        this.ticker = ticker;
        this.relevance_score = relevance_score;
        this.ticker_sentiment_score = ticker_sentiment_score;
        this.ticker_sentiment_label = ticker_sentiment_label;
    }

    // Getters and setters

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getRelevanceScore() {
        return relevance_score;
    }

    public void setRelevanceScore(String relevance_score) {
        this.relevance_score = relevance_score;
    }

    public String getTickerSentimentScore() {
        return ticker_sentiment_score;
    }

    public void setTickerSentimentScore(String ticker_sentiment_score) {
        this.ticker_sentiment_score = ticker_sentiment_score;
    }

    public String getTickerSentimentLabel() {
        return ticker_sentiment_label;
    }

    public void setTickerSentimentLabel(String ticker_sentiment_label) {
        this.ticker_sentiment_label = ticker_sentiment_label;
    }
}
