package com.citi.exchange;

import com.citi.exchange.algorithms.TMA;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TMATest {
    TMA tma;

    @Before
    public void setUp(){
        tma = new TMA();
    }

    @Test
    public void calculateAvgWithAllPositiveNumbersAndFullWindowSize(){
        List<Double> priceList = Arrays.asList(10.50, 9.89, 5.90, 7.90);
        int windowSize = 4;

        assertEquals(8.5475, tma.getAverage(priceList, windowSize), 0.005);
    }

    @Test
    public void calculateAvgWithANegativeNumberAndFullWindowSize(){
        List<Double> priceList = Arrays.asList(10.50, -9.89, 5.90, 7.90);
        int windowSize = 4;

        assertEquals(3.6025, tma.getAverage(priceList, windowSize), 0.005);
    }


    @Test
    public void calculateAvgWithALLPositiveNumbersAndHalfWindowSize(){
        List<Double> priceList = Arrays.asList(10.50, 9.89, 5.90, 7.90);
        int windowSize = 2;

        assertEquals(10.195, tma.getAverage(priceList, windowSize), 0.005);
    }

    @Test
    public void calculateAvgWithEmptyPriceList(){
        List<Double> priceList = Arrays.asList();
        int windowSize = 2;

        assertEquals(0.0, tma.getAverage(priceList, windowSize), 0.005);
    }

}
