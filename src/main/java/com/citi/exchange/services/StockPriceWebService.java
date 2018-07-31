package com.citi.exchange.services;


import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StockPrice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Scanner;

@Component
public class StockPriceWebService {
    private double price;
    private String marketFeedUrl;
    private String ticker;

    private Stock stock;
    private StockPrice stockPrice;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private StockService stockService;

    @Autowired
    public StockPriceWebService(@Value("${market.feed.url}") String marketFeedUrl){
        this.marketFeedUrl = marketFeedUrl;
    }

    public StockPriceWebService() {}

    //First run MockYahoo server (Tomcat) before running ExchangeApplication
    //@Scheduled(fixedRate = 1000)
    public void getMarketPrice(){
        try {
            Collection<Stock> stocks = stockService.getStocks();
            for(Stock stock : stocks) {
                String ticker = stock.getTicker().replaceAll("\\s", "");

                if(isValidTicker(ticker)){
                    setStockPrice(getResponseFromURL(ticker));
                    java.sql.Timestamp currentTime = getCurrentTimeStamp();
                    stockPrice = new StockPrice(currentTime, stock, price);
                    stockPriceService.addNewStockPrice(stockPrice);
                } else
                    throw new RuntimeException("Invalid ticker - cannot get market price");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Timestamp getCurrentTimeStamp(){
        return new Timestamp(System.currentTimeMillis());
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