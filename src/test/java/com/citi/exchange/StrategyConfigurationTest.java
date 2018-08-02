package com.citi.exchange;

import com.citi.exchange.entities.StrategyConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrategyConfigurationTest {
    private StrategyConfiguration strategy;

    @Before
    public void setUp(){
        strategy = new StrategyConfiguration();
    }

    @Test
    public void returnPositivePercentageOfPNLGainOrLoss(){
        double priceSold = 3800;
        double investmentValue = 3000;
        double currentPNL = priceSold - investmentValue;

        assertEquals(26.6667, strategy.getGainOrLossFromPNL(currentPNL, investmentValue), 0.0);
    }

    @Test
    public void returnNegativePercentageOfPNLGainOrLoss(){
        double priceSold = 3000;
        double investmentValue = 3800;
        double currentPNL = priceSold - investmentValue;

        assertEquals(-21.0526, strategy.getGainOrLossFromPNL(currentPNL, investmentValue), 0.0);
    }

}
