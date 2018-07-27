package com.citi.exchange;

import com.citi.exchange.services.StockPriceGetterService;
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
        StockPriceGetterService service = new StockPriceGetterService();
        service.getStockPrice("goog");
        double price = service.getPrice();
        assertTrue(price > 0);
    }

    @Test
    public void getMarketPriceFromStockPriceGetterServiceWithEmptySpaceTicker(){
        StockPriceGetterService service = new StockPriceGetterService();
        service.getStockPrice(" ");
        double price = service.getPrice();
        assertEquals((int)0.0, (int)price);
    }

    @Test
    public void getMarketPriceFromStockPriceGetterServiceWithNoTicker(){
        StockPriceGetterService service = new StockPriceGetterService();
        service.getStockPrice("");
        double price = service.getPrice();
        assertEquals((int)0.0, (int)price);
    }
}
