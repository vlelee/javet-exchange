package com.citi.exchange;

import com.citi.exchange.algorithms.StrategyExecution;

import com.citi.exchange.entities.StrategyConfiguration;
import org.junit.Before;
import org.junit.Test;


public class StrategyExecutionTest {
    private StrategyExecution strategyExecution;
    private StrategyConfiguration strategyConfiguration;

    @Before
    public void setUp(){
        strategyExecution = new StrategyExecution();
        strategyConfiguration = new StrategyConfiguration();
    }

    @Test
    public void isActiveStrategyReturnsTrueIfDoesNotReachExitThreshold(){
        //strategyConfiguration.set
    }
}
