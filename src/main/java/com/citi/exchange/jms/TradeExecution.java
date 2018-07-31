package com.citi.exchange.jms;

import java.text.SimpleDateFormat;

public class TradeExecution {

    public static void sendMessage(String msg) {

        MessageSender jmsMessageSender = new MessageSender();
        jmsMessageSender.send(msg);

        try
        {
            // give time for the receiver to get the messages
            Thread.sleep(100);
        } catch (
                InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public String createOrderMessage(Boolean buy, int tradeId, double price, int shares, String ticker, java.sql.Timestamp timestamp) {
        return "<trade>\n" +
                "<buy>" + buy + "</buy>\n" +
                "<id>" + tradeId + "</id>\n" +
                "<price>" + price + "</price>\n" +
                "<size>" + shares + "</size>\n" +
                "<stock>" + ticker + "</stock>\n" +
                "<whenAsDate>" + (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).format(timestamp) + "</whenAsDate>\n" +
                "</trade>";
    }


}
