package com.sutock2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {

    private String id;

    private String title;
    private String summary;
    @SerializedName("url")
    private String url;
    private String time_published;
    @SerializedName("bannerImage")
    private String banner_image;
    private String source;
    private String category_within_source;
    private String source_domain;

    @SerializedName("tickerSentiment")
    private List<TickerSentiment> ticker_sentiment;

    public News(String title, String summary, String url, String time_published,
                String banner_image, String source, String category_within_source, String source_domain,
                List<TickerSentiment> ticker_sentiment) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.time_published = time_published;
        this.banner_image = banner_image;
        this.source = source;
        this.category_within_source = category_within_source;
        this.source_domain = source_domain;
        this.ticker_sentiment = ticker_sentiment;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimePublished() {
        return time_published;
    }

    public void setTimePublished(String time_published) {
        this.time_published = time_published;
    }

    public String getBannerImage() {
        return banner_image;
    }

    public void setBannerImage(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategoryWithinSource() {
        return category_within_source;
    }

    public void setCategoryWithinSource(String category_within_source) {
        this.category_within_source = category_within_source;
    }

    public String getSourceDomain() {
        return source_domain;
    }


    public void setSourceDomain(String source_domain) {
        this.source_domain = source_domain;
    }

    public List<TickerSentiment> getTickerSentiment() {
        return ticker_sentiment;
    }

    public void setTickerSentiment(List<TickerSentiment> ticker_sentiment) {
        this.ticker_sentiment = ticker_sentiment;
    }
}
