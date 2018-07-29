package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;

public class TMA implements StrategyExecution {
    boolean previousSAExceedsLA = false; // previous short average exceeds long average

    private static final int longAveragePeriod = 240; // Default: 4 hours
    private static final int shortAveragePeriod = 30; // Default: 30 minutes

    @Override
    public void initiate(StrategyConfiguration s) {
        // TODO: set previousSAExceedsLA somehow...
    }

    @Override
    public void exit(StrategyConfiguration s) {

    }



    /*
    public double runTMA() {
        double newShortAverage = super.getAverageForPast(shortAveragePeriod);
        double newLongAverage = super.getAverageForPast(longAveragePeriod);

        boolean buying = ??;
        if(buying) {
            // If we're buying and the last SA < last LA and current SA > current LA -> buy
            if(!previousSAExceedsLA && newShortAverage > newLongAverage) {
                buy();
            }
        } else {
            // If we're buying and the last SA > last LA and current SA < current LA -> buy
            if(previousSAExceedsLA && newShortAverage < newLongAverage) {
                sell();
            }
        }

        previousSAExceedsLA = newShortAverage > newLongAverage;
    }

    */
}


