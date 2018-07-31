package com.citi.exchange.services;


import com.citi.exchange.entities.Stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class StockPriceWebService {
    private double price;
    private String marketFeedUrl;
    private String ticker;

    @Autowired
    private StockService stockService;

    @Autowired
    public StockPriceWebService(@Value("${market.feed.url}") String marketFeedUrl){
        this.marketFeedUrl = marketFeedUrl;
    }

    public StockPriceWebService() {}

    //First run MockYahoo server (Tomcat) before running ExchangeApplication
    public Map<String, Double> getMarketPrice(){
        Map<String, Double> prices = new HashMap<>();
        try {
            Collection<Stock> stocks = stockService.getStocks();
            for(Stock stock : stocks) {
                ticker = stock.getTicker().trim();
                if(isValidTicker(ticker)){
                    prices.put(ticker, getResponseFromURL(ticker));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prices;
    }


    private boolean isValidTicker(String ticker){
        if(ticker.equals("") || ticker.equals(" "))
            return false;
        return true;
    }

    private double getResponseFromURL(String ticker) throws IOException {
        URL url = new URL(marketFeedUrl + "?s="+ ticker + "&f=p0");
        Scanner scanner = new Scanner(url.openStream());

        return Double.parseDouble(scanner.next());
    }


    public double getStockPrice() {
        return price;
    }

    public void setStockPrice(double stockPrice) {
        this.price = stockPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}