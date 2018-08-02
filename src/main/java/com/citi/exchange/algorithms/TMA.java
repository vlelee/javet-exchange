package com.citi.exchange.algorithms;


import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.jms.TradeExecution;
import com.citi.exchange.services.StrategyService;
import com.citi.exchange.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
public class TMA implements Strategy {
    boolean previousSAExceedsLA = false; // previous short average exceeds long average

    private static final int longAveragePeriod = 24; // Default: 4 hours
    private static final int shortAveragePeriod = 3; // Default: 30 minutes

    private List<Double> windowQueue = new ArrayList<>();

    @Autowired
    TradeService tradeService;

    @Autowired
    TradeExecution tradeExecution;

    @Autowired
    StrategyService strategyService;


    int index = 0;
    private StrategyConfiguration strategyConfiguration;
//
//    public TMA(StrategyConfiguration strategyConfiguration){
//        this.strategyConfiguration = strategyConfiguration;
//    }

    @Override
    public void run(double newPrice) {
        windowQueue.add(0, newPrice);
        if(windowQueue.size() >= longAveragePeriod){

            double newShortAverage = getAverage(windowQueue, shortAveragePeriod);
            double newLongAverage = getAverage(windowQueue, longAveragePeriod);
//            index += 1;
//            if(index % 40 == 0) {
//                System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Short: " + newShortAverage + " Long: " + newLongAverage);
//            }
            strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());

            boolean buying = strategyConfiguration.isBuyingAdvanced();
            int stockQuantity = (int) Math.floor(strategyConfiguration.currentInvestmentValue() / newPrice);
            if(buying){
                // If we're buying and the last SA > last LA and current SA < current LA -> buy
                if(previousSAExceedsLA && newShortAverage < newLongAverage) {
                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Buying " + stockQuantity +  " @ " + newPrice + "Strategy buying: " + strategyConfiguration.isBuyingAdvanced());
                    Trade buyTrade = tradeService.addNewTrade(new Trade(true, stockQuantity, newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(buyTrade);

                    strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());
                    System.out.println("Investment Value: " + strategyConfiguration.currentInvestmentValue() + ", Strategy PnL " + strategyConfiguration.currentPnL());
                    //strategyConfiguration.setBuying(false);
                }
            } else {
                // If we're buying and the last SA < last LA and current SA > current LA -> buy
                if(!previousSAExceedsLA && newShortAverage > newLongAverage) {

                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Selling " + stockQuantity + " @ " + newPrice + "Strategy buying: " + strategyConfiguration.isBuyingAdvanced());
                    Trade sellTrade = tradeService.addNewTrade(new Trade(false, strategyConfiguration.getStockHeld(), newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(sellTrade);

                    strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());
                    System.out.println("Investment Value: " + strategyConfiguration.currentInvestmentValue() + ", Strategy PnL " + strategyConfiguration.currentPnL());
                    //strategyConfiguration.setBuying(true);
                }
            }

            previousSAExceedsLA = newShortAverage > newLongAverage;

        }
    }

    @Override
    public void exit() {

    }

    public double getAverage(List<Double> windowQueque, int windowSize) {

        return windowQueque.stream()
                .limit(windowSize)
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }

    public StrategyConfiguration getStrategyConfiguration() {
        return strategyConfiguration;
    }

    public void setStrategyConfiguration(StrategyConfiguration strategyConfiguration) {
        this.strategyConfiguration = strategyConfiguration;
    }



}


