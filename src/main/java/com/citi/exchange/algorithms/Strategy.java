package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;

public interface Strategy {
    //StrategyConfiguration config;

    public void run(double newPrice);

    public void exit();

}
