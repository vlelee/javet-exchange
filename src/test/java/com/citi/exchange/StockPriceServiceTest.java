package com.citi.exchange;


import com.citi.exchange.services.StockPriceWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockPriceServiceTest {

    @Autowired
    StockPriceWebService service;

    @Test
    public void getGoogleMarketPriceFromStockPriceGetterServiceWithGoogleTicker(){
        service.setTicker("GOOG");
        service.getMarketPrice();

        double stockPrice = service.getStockPrice();

        assertTrue(stockPrice > 0);
    }
    
//TODO: ADD mockito and mock runtime error
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
