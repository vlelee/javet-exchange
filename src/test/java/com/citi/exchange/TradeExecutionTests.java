package com.citi.exchange;

import com.citi.exchange.algorithms.TradeExecution;
import com.citi.exchange.services.StockPriceWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeExecutionTests {
    @Test
    public void getMessageConversion(){
        TradeExecution servce = new TradeExecution();
        java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
        String message = servce.createMessage(true, 123, 11.50, 200, "GOOGL", time );

        String expected = "<trade>\n" +
                "<buy>true</buy>\n" +
                "<id>123</id>\n" +
                "<price>11.5</price>\n" +
                "<size>200</size>\n" +
                "<stock>GOOGL</stock>\n" +
                "<whenAsDate>" + (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).format(time) +"</whenAsDate>\n" +
                "</trade>";

        assertEquals(message, expected );


    }

//    @Test
//    public void getMarketPriceFromStockPriceGetterServiceWithEmptySpaceTicker(){
//        StockPriceWebService service = new StockPriceWebService();
//        service.getMarketPrice(" ");
//
//        double stockPrice = service.getStockPrice();
//        assertEquals((int)0.0, (int)stockPrice);
//    }
//
//    @Test
//    public void getMarketPriceFromStockPriceGetterServiceWithNoTicker(){
//        StockPriceWebService service = new StockPriceWebService();
//        service.getMarketPrice("");
//
//        double stockPrice = service.getStockPrice();
//        assertEquals((int)0.0, (int)stockPrice);
//    }
}
