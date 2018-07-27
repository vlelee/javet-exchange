package com.citi.exchange.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class StockPriceWebService {
    private double price;


    public void getStockPrice(String ticker){

        //First run MockYahoo server (Tomcat) before running ExchangeApplication
        try {
            if(checkTicker(ticker)){
                URL marketFeedURL = new URL("http://localhost:8081/MockYahoo/quotes.csv?s="
                        + ticker + "&f=p0");
                URLConnection connection = marketFeedURL.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String input;
                if((input = reader.readLine()) != null){
                    price = Double.parseDouble(input);
                    System.out.println("Price for " + ticker + ": " + price);
                }
                else {
                    System.out.println("Stock Price Service error");
                }
            } else {
                price = 0;
                System.out.println("Bad ticker error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkTicker(String ticker){
        if(ticker.equals("") || ticker.equals(" "))
            return false;
        return true;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}