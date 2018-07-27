package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;

public class TMA implements StrategyExecution {
    @Override
    public void initiate(StrategyConfiguration s) {

    }

    @Override
    public void exit(StrategyConfiguration s) {

    }


    Double shortAverage = null;
    Double longAverage = null;
    boolean previousSAExceedsLA = false; // previous short average exceeds long average

    // TODO: DISCUSS whether this should be a DB column and modifiable by the end-user.
    private static final int longAveragePeriod = 240; // Default: 4 hours
    private static final int shortAveragePeriod = 30; // Default: 30 minutes

//    public double calculateMovingAverages() {
//        double newShortAverage = super.getAverageForPast(shortAveragePeriod);
//    }

}


