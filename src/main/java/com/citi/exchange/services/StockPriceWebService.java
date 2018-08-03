package com.citi.exchange.services;


import com.citi.exchange.entities.Stock;

import com.citi.exchange.entities.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class StockPriceWebService {
    private double price;
    private String marketFeedUrl = "http://feed.conygre.com:8080/MockYahoo/quotes.csv";
    private String ticker;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockPriceService stockPriceService;


    public StockPriceWebService() {}

    //First run MockYahoo server (Tomcat) before running ExchangeApplication
    public Map<String, Double> getMarketPrice(){
        Map<String, Double> prices = new HashMap<>();

        try {
            Collection<Stock> stocks = stockService.getStocks();
            for(Stock stock : stocks) {
                ticker = stock.getTicker().trim();
                if(isValidTicker(ticker)){

                    Double price = getResponseFromURL(ticker);
                    prices.put(ticker, price);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    StockPrice stockPrice = new StockPrice(timestamp, stock, price);
                    stockPriceService.addNewStockPrice(stockPrice);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prices;
    }


    public boolean isValidTicker(String ticker){
        if(ticker.equals("") || ticker.equals(" "))
            return false;
        return true;
    }

    public double getResponseFromURL(String ticker) throws IOException {
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