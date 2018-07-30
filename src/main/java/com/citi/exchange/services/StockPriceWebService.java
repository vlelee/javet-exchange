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
import java.util.Scanner;

@Component
public class StockPriceWebService {
    private double price;

    private String ticker;

    private Stock stock;
    private StockPrice stockPrice;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private StockService stockService;

    //private String marketFeedUrl="http://localhost:8081/MockYahoo/quotes.csv";

    private String marketFeedUrl;

    @Autowired
    public StockPriceWebService(@Value("${market.feed.url}") String marketFeedUrl){
        this.marketFeedUrl = marketFeedUrl;
    }


    public StockPriceWebService() {}

    //@Scheduled(fixedRate = 1000)
    public void getMarketPrice(){
        //ticker = ticker.replaceAll("\\s","");

        //First run MockYahoo server (Tomcat) before running ExchangeApplication
        try {
            if(isValidTicker(ticker)) {
                setStockPrice(getResponseFromURL(ticker));

                java.sql.Timestamp currentTime = getCurrentTimeStamp();
                stock = new Stock(ticker, "tempName", true);

                stockService.addNewStock(stock);

                stockPrice = new StockPrice(currentTime, stock, price);

                stockPriceService.addNewStockPrice(stockPrice);

                System.out.println(ticker + " " + price + " time: " + getCurrentTimeStamp());
            }
            else
                throw new RuntimeException("Invalid ticker");

            //stockService = new StockService();
            //stockPriceService = new StockPriceService();


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

    private Timestamp getCurrentTimeStamp(){
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