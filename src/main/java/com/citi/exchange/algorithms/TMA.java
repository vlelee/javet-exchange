package com.citi.exchange.algorithms;


import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.jms.TradeExecution;
import com.citi.exchange.services.StrategyService;
import com.citi.exchange.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
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

    //Two Moving Averages algorithm
    @Override
    public void run(double newPrice) {
        windowQueue.add(0, newPrice);
        if(windowQueue.size() >= longAveragePeriod){

            double newShortAverage = getAverage(windowQueue, shortAveragePeriod);
            double newLongAverage = getAverage(windowQueue, longAveragePeriod);
            System.out.println("Strategy " + strategyConfiguration.getStrategyName() + " Short avg: " + newShortAverage + " Long avg: " + newLongAverage);
            strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());

            boolean buying = strategyConfiguration.isCurrentlyBuying();
            int stockQuantity = (int) Math.floor(strategyConfiguration.currentInvestmentValue() / newPrice);
            if(buying){
                // If we're buying and the last SA > last LA and current SA < current LA -> buy
                if(previousSAExceedsLA && newShortAverage < newLongAverage) {
                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Buying " + stockQuantity +  " @ " + newPrice + "Strategy buying: " + strategyConfiguration.isCurrentlyBuying());

                    Trade buyTrade = tradeService.addNewTrade(new Trade(true, stockQuantity, newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(buyTrade);

                    strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());
//                    double initialPrice = strategyConfiguration.getNumShares() * strategyConfiguration.getInitiationPrice();
//                    System.out.println("Investment Value: " + strategyConfiguration.currentInvestmentValue() + ", Strategy PnL "
//                     + strategyConfiguration.currentPnL(strategyConfiguration.currentInvestmentValue(), initialPrice) + " GOL: "
//                            + (strategyConfiguration.currentPnL()/strategyConfiguration.currentInvestmentValue())*100 + "%");

                }
            } else {
                // If we're selling and the last SA < last LA and current SA > current LA -> buy
                if(!previousSAExceedsLA && newShortAverage > newLongAverage) {
                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Selling " + stockQuantity + " @ " + newPrice + "Strategy buying: " + strategyConfiguration.isCurrentlyBuying());

                    Trade sellTrade = tradeService.addNewTrade(new Trade(false, strategyConfiguration.getSharesCurrentlyHeld(), newPrice, strategyConfiguration.getStock(), strategyConfiguration));
                    tradeExecution.send(sellTrade);

                    strategyConfiguration = strategyService.getStrategyById(strategyConfiguration.getId());
//                    double initialPrice = strategyConfiguration.getNumShares() * strategyConfiguration.getInitiationPrice();
//                    System.out.println("Investment Value: " + strategyConfiguration.currentInvestmentValue() + ", Strategy PnL "
//                     + strategyConfiguration.currentPnL(strategyConfiguration.currentInvestmentValue(), initialPrice) + " GOL: "
//                            + (strategyConfiguration.currentPnL()/strategyConfiguration.currentInvestmentValue())*100 + "%");

                }
            }

            previousSAExceedsLA = newShortAverage > newLongAverage;

        }
    }

    //Computes the average of a list of prices in windowQueue
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


