package com.citi.exchange.algorithms;

import java.text.SimpleDateFormat;

public class TradeExecution {


    public String createMessage(Boolean buy, int id, double price, int shares, String ticker, java.sql.Timestamp timestamp){
       String message = "<trade>\n" +
               "<buy>" + buy + "</buy>\n" +
               "<id>" + id + "</id>\n" +
               "<price>" + price + "</price>\n" +
               "<size>" + shares + "</size>\n" +
               "<stock>" + ticker + "</stock>\n" +
               "<whenAsDate>" + (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).format(timestamp) +"</whenAsDate>\n" +
               "</trade>";

        return message;
    }

}
