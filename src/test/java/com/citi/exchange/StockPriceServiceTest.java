package com.citi.exchange;


import com.citi.exchange.services.StockPriceWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockPriceServiceTest {

    @Autowired
    StockPriceWebService service;

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


}
