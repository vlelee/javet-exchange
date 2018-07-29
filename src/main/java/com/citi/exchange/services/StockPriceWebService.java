package com.citi.exchange.services;

import com.citi.exchange.entities.StockPrice;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Scanner;

public class StockPriceWebService {
    //private StockPrice stockPrice;
    private double price;

    public void getMarketPrice(String ticker){
        //First run MockYahoo server (Tomcat) before running ExchangeApplication
        try {
            if(checkTicker(ticker)){
                price = getResponseFromURL(ticker);
            } else {
                price = 0.0;
                System.out.println("Invalid Ticker");
            }

        } catch (Exception e) {
            throw new RuntimeException("Market Feed network failure");
        }

    }

    private boolean checkTicker(String ticker){
        if(ticker.equals("") || ticker.equals(" "))
            return false;
        return true;
    }

    private double getResponseFromURL(String ticker) throws IOException {
        URL marketFeedUrl = new URL("http://localhost:8081/MockYahoo/quotes.csv?s="
                + ticker + "&f=p0");
        Scanner scanner = new Scanner(marketFeedUrl.openStream());

        return Double.parseDouble(scanner.next());
    }

    private Timestamp getCurrentTimeStamp(){
        return new Timestamp(System.currentTimeMillis());
    }


    public double getStockPrice() {
        return price;
    }

    public void setStockPrice(double stockPrice) {
        this.price = stockPrice;
    }
}