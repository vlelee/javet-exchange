package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;

public interface StrategyExecution {

    public void initiate(StrategyConfiguration s);

    public void exit(StrategyConfiguration s);

}
