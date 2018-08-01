package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.services.StockPriceWebService;
import com.citi.exchange.services.StrategyService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class StrategyExecution {
    private Map<Integer, TMA> activeTMAStrategies = new HashMap<>();

    @Autowired
    private StockPriceWebService stockPriceWebService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private BeanFactory beanFactory;

    @Scheduled(fixedRate = 50)
    public void execute(){
        Map<String, Double> marketPrice = stockPriceWebService.getMarketPrice();
        searchForConfigurations();
        for(TMA strategy : activeTMAStrategies.values()){
            double currentPrice = marketPrice.get(strategy.getStrategyConfiguration().getStock().getTicker());
            //System.out.println("ticker: " + strategy.getStrategyConfiguration().getStock().getTicker() + " current price: " + currentPrice);
            strategy.run(currentPrice);
        }
    }

    public void searchForConfigurations(){
        Collection<StrategyConfiguration> strategies = strategyService.getActiveStrategies();
        for(StrategyConfiguration strategy: strategies){
            switch(strategy.getAlgo()){
                case TMA: default:
                    if(!activeTMAStrategies.containsKey(strategy.getId())){
                        TMA tma = beanFactory.getBean(TMA.class);
                        tma.setStrategyConfiguration(strategy);
                        activeTMAStrategies.put(strategy.getId(), tma);
                        //System.out.println("New Strategy '" + strategy.getStrategyName() + "' with investment value: " + strategy.currentInvestmentValue());
                    }
            }

        }

    }


}
