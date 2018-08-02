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
    public void returnPositivePercentageOfGainOrLossOfInvestment(){
        double priceSold = 3800;
        double investmentValue = 3000;
        double currentPNL = priceSold - investmentValue;

        assertEquals(26.6667, strategy.getGainOrLossFromPNL(currentPNL, investmentValue), 0.0);
    }

    @Test
    public void returnNegativePercentageOfGainOrLossOfInvestment(){
        double priceSold = 3000;
        double investmentValue = 3800;
        double currentPNL = priceSold - investmentValue;

        assertEquals(-21.0526, strategy.getGainOrLossFromPNL(currentPNL, investmentValue), 0.0);
    }

    @Test
    public void returnZeroPercentageOfGainOrLossOfInvestment(){
        double priceSold = 3000;
        double investmentValue = 0;
        double currentPNL = priceSold - investmentValue;

        assertEquals(0.0, strategy.getGainOrLossFromPNL(currentPNL, investmentValue), 0.0);
    }

    @Test
    public void getPositiveCurrentPNLFromInvestMentValueAndInitialPrice(){
        double investmentValue = 1500;
        double initialPrice = 1450;

        assertEquals(50.0, strategy.currentPnL(investmentValue, initialPrice), 0.0);
    }

    @Test
    public void getNegativeCurrentPNLFromInvestMentValueAndInitialPrice(){
        double investmentValue = 1500;
        double initialPrice = 1550;

        assertEquals(-50.0, strategy.currentPnL(investmentValue, initialPrice), 0.0);
    }

}
