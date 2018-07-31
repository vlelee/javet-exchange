package com.citi.exchange.algorithms;


import com.citi.exchange.entities.StrategyConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TMA implements Strategy {
    boolean previousSAExceedsLA = false; // previous short average exceeds long average

    private static final int longAveragePeriod = 24; // Default: 4 hours
    private static final int shortAveragePeriod = 3; // Default: 30 minutes

    private List<Double> windowQueue = new ArrayList<>();

    public StrategyConfiguration getStrategyConfiguration() {
        return strategyConfiguration;
    }

    public void setStrategyConfiguration(StrategyConfiguration strategyConfiguration) {
        this.strategyConfiguration = strategyConfiguration;
    }

    private StrategyConfiguration strategyConfiguration;

    public TMA(StrategyConfiguration strategyConfiguration){
        this.strategyConfiguration = strategyConfiguration;
    }

    @Override
    public void run(double newPrice) {
        windowQueue.add(0, newPrice);
        if(windowQueue.size() >= longAveragePeriod){

            double newShortAverage = getAverage(windowQueue, shortAveragePeriod);
            double newLongAverage = getAverage(windowQueue, longAveragePeriod);
            System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Short: " + newShortAverage + " Long: " + newLongAverage);

            boolean buying = strategyConfiguration.isBuying();
            if(buying){
                // If we're buying and the last SA < last LA and current SA > current LA -> buy
                if(!previousSAExceedsLA && newShortAverage > newLongAverage) {
                    //buy();
                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Buying @ " + newPrice);
                    strategyConfiguration.setBuying(false);
                }
            } else {
                // If we're buying and the last SA > last LA and current SA < current LA -> buy
                if(previousSAExceedsLA && newShortAverage < newLongAverage) {
                    //sell();
                    System.out.println("Strategy name: " + strategyConfiguration.getStrategyName() + " Selling @ " + newPrice);
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



}


