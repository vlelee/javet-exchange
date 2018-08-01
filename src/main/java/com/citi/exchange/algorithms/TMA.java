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
            //System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Short: " + newShortAverage + " Long: " + newLongAverage);
            strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());

            boolean buying = strategyConfiguration.isBuying();
            if(buying){
                // If we're buying and the last SA < last LA and current SA > current LA -> buy
                if(!previousSAExceedsLA && newShortAverage > newLongAverage) {
                    Trade buyTrade = tradeService.addNewTrade(new Trade(true, strategyConfiguration.getNumShares(), newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(buyTrade);

                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Buying @ " + newPrice);
                    //System.out.println("Strategy buying: " + strategyConfiguration.isBuyingAdvanced() + " Investment Value: " + strategyConfiguration.currentInvestmentValue());
                    strategyConfiguration.setBuying(false);
                }
            } else {
                // If we're buying and the last SA > last LA and current SA < current LA -> buy
                if(previousSAExceedsLA && newShortAverage < newLongAverage) {

                    Trade sellTrade = tradeService.addNewTrade(new Trade(false, strategyConfiguration.getNumShares(), newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(sellTrade);

                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Selling @ " + newPrice);
                    //System.out.println("Strategy buying: " + strategyConfiguration.isBuyingAdvanced() + " Investment Value: " + currentInvestmentValue());
                    strategyConfiguration.setBuying(true);
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


