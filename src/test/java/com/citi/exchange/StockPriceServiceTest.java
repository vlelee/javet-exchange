package com.citi.exchange;


import com.citi.exchange.services.StockPriceWebService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StockPriceServiceTest {

    StockPriceWebService service;

    @Before
    public void setUp(){
        service = new StockPriceWebService();
    }

    @Test
    public void getGoogleMarketPriceFromStockPriceGetterServiceWithGoogleTicker(){
        double stockPrice = 0;
        try {
            stockPrice = service.getResponseFromURL("GOOG");

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(stockPrice > 0);
    }

    @Test
    public void isGoogAValidTicker(){
        assertTrue(service.isValidTicker("Goog"));
    }

    @Test
    public void isLowercaseCAValidTicker(){
        assertTrue(service.isValidTicker("c"));
    }

    @Test
    public void isNoStringAValidTicker(){
        assertFalse(service.isValidTicker(""));
    }

    @Test
    public void isEmptySpaceAValidTicker()
    {
        assertFalse(service.isValidTicker(" "));
    }

}
