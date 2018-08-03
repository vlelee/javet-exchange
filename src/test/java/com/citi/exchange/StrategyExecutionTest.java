package com.citi.exchange;

import com.citi.exchange.algorithms.StrategyExecution;
import com.citi.exchange.algorithms.TMA;
import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class StrategyExecutionTest {
    private StrategyExecution strategyExecution;
    private TMA tma;
    private Map<Integer, TMA> activeStrategies;
    private Stock citiStock;


    @Before
    public void setUp(){
        strategyExecution = new StrategyExecution();
        tma = new TMA();
        activeStrategies = strategyExecution.getActiveTMAStrategies();
        citiStock = new Stock("C", "Citi");

    }


    @Test
    public void returnsTrueWhenDidNotReachHighExitThreshold(){
        StrategyConfiguration strategyConfiguration = new StrategyConfiguration("StratTest", StrategyConfiguration.Algo.valueOf("TMA"),
                new Timestamp(System.currentTimeMillis()), 50.0, 10, true, true, 10.5, 10.5, citiStock);
        strategyConfiguration.setId(2);
        tma.setStrategyConfiguration(strategyConfiguration);
        activeStrategies.put(2, tma);

        double investmentValue = 510.0;
        boolean isActiveStrategy = strategyExecution.isAnActiveStrategy(strategyConfiguration, investmentValue);
        strategyConfiguration.setActive(false);

        assertTrue(isActiveStrategy);


    }

    @Test
    public void returnsTrueWhenDidNotReachLowExitThreshold(){
                StrategyConfiguration strategyConfiguration = new StrategyConfiguration("StratTest", StrategyConfiguration.Algo.valueOf("TMA"),
                new Timestamp(System.currentTimeMillis()), 50.0, 10, true, true, 10.5, 10.5, citiStock);
        strategyConfiguration.setId(2);
        tma.setStrategyConfiguration(strategyConfiguration);
        activeStrategies.put(2, tma);

        double investmentValue = 499.0;

        boolean isActiveStrategy = strategyExecution.isAnActiveStrategy(strategyConfiguration, investmentValue);
        strategyConfiguration.setActive(false);

        assertTrue(isActiveStrategy);
    }
}

