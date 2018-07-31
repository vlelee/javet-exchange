package com.citi.exchange;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.jms.TradeExecution;
import com.citi.exchange.services.StockService;
import com.citi.exchange.services.StrategyService;
import com.citi.exchange.services.TradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {com.citi.exchange.ExchangeApplication.class})

public class TradeExecutionTests {

    @Autowired
    private TradeExecution tradeExecutionService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StrategyService strategyService;

    @Test
    public void testMessageSend() {
        java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
        Stock stock = stockService.getStockByTicker("GOOGL");
        StrategyConfiguration strategyConfiguration = strategyService.getStrategyById(1);
        Trade newTrade =tradeService.addNewTrade(new Trade(true, 20, 100.50, time, stock, strategyConfiguration));
        tradeExecutionService.send(newTrade);
    }

}
