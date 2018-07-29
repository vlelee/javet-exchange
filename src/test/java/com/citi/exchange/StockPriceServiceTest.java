package com.citi.exchange;

import com.citi.exchange.services.StockPriceWebService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockPriceServiceTest {
    @Test
    public void getGoogleMarketPriceFromStockPriceGetterServiceWithGoogleTicker(){
        StockPriceWebService service = new StockPriceWebService();
        service.getMarketPrice("goog");

        double stockPrice = service.getStockPrice();

        assertTrue(stockPrice > 0);
    }

    @Test
    public void getMarketPriceFromStockPriceGetterServiceWithEmptySpaceTicker(){
        StockPriceWebService service = new StockPriceWebService();
        service.getMarketPrice(" ");

        double stockPrice = service.getStockPrice();
        assertEquals((int)0.0, (int)stockPrice);
    }

    @Test
    public void getMarketPriceFromStockPriceGetterServiceWithNoTicker(){
        StockPriceWebService service = new StockPriceWebService();
        service.getMarketPrice("");

        double stockPrice = service.getStockPrice();
        assertEquals((int)0.0, (int)stockPrice);
    }
}
