package com.sutock2;

import java.math.BigDecimal;
public class StockPrice {

    private Float open;
    private Float high;
    private Float low;
    private Float close;

    public StockPrice(Float open, Float high, Float low, Float close) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getLow() {
        return low;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }
}
