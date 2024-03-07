package com.sutock2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Stock {
    @SerializedName("id")
    private String id;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("companyName")
    private String companyName;
    //@SerializedName("relatedNews")
    //private News relatedNews;
    //@SerializedName("addedByUser")
    //private User addedByUser;
    @SerializedName("prices")
    private List<StockPrice> prices;
    // Constructor
    public Stock(String id, String symbol, String companyName, List<StockPrice> prices) {
        this.id = id;
        this.symbol = symbol;
        this.companyName = companyName;
        this.prices = prices;
    }

    public List<StockPrice> getPrices() {
        return prices;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getId() {
        return id;
    }
}
