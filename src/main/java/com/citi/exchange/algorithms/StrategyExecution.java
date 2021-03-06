package com.citi.exchange.algorithms;

import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.marketfeed.MockFeedParser;
import com.citi.exchange.services.StrategyService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class StrategyExecution {
    private Map<Integer, TMA> activeTMAStrategies = new HashMap<>();
    private boolean printedOnce = false;

    @Autowired
    private MockFeedParser mockFeedParser;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private BeanFactory beanFactory;

    //Run active strategies and remove inactive strategies at a continuous interval
    @Scheduled(fixedRate = 100)
    public void execute(){
        Map<String, Double> marketPrice = mockFeedParser.getMarketPrice();
        searchForConfigurations();

        Iterator<TMA> iterator = activeTMAStrategies.values().iterator();
        while(iterator.hasNext()) {
            TMA strategy = iterator.next();

            if(strategy.getStrategyConfiguration().isActive()){
                String ticker = strategy.getStrategyConfiguration().getStock().getTicker();

                if(ticker != null && marketPrice.containsKey(ticker)) {
                    double currentPrice = marketPrice.get(ticker);
                    strategy.run(currentPrice);

                    isAnActiveStrategy(strategy.getStrategyConfiguration(),
                            strategy.getStrategyConfiguration().currentInvestmentValue());
                }
            }  else {
                iterator.remove();
            }
        }
        if(activeTMAStrategies.isEmpty() && !printedOnce){
            System.out.println("No current active strategy");
            printedOnce = true;
        }
    }

    //Initialize active strategies map with strategy id as key and TMA instance as value
    public void searchForConfigurations(){
        Collection<StrategyConfiguration> strategies = strategyService.getActiveStrategies();
        for(StrategyConfiguration strategy: strategies){
            switch(strategy.getAlgo()){
                case TMA: default:
                    if(!activeTMAStrategies.containsKey(strategy.getId())){
                        TMA tma = beanFactory.getBean(TMA.class);
                        tma.setStrategyConfiguration(strategy);
                        activeTMAStrategies.put(strategy.getId(), tma);
                    }
            }
        }
    }

    //Deactivate current strategies that passes the exit threshold set by user
    public boolean isAnActiveStrategy(StrategyConfiguration strategyConfiguration, double investmentVal){
        //StrategyConfiguration strategyConfiguration = strategyService.getStrategyById(strategyConfig.getId());
        double initialPrice = strategyConfiguration.getNumShares() * strategyConfiguration.getInitiationPrice();
        double currentPNL = strategyConfiguration.currentPnL(investmentVal, initialPrice);
        double percentageGoL = strategyConfiguration.getGainOrLossFromPNL(currentPNL, investmentVal); //determines the percentage of gain or loss on an investment
        //System.out.println("initial price: " + initialPrice + " PNL: " + currentPNL + " GoL: " + percentageGoL);

        if(!strategyConfiguration.isActive()){
            strategyService.deactivateStrategy(strategyConfiguration.getId());
            return false;
        }
        else {
            if(activeTMAStrategies.containsKey(strategyConfiguration.getId())){
                if(percentageGoL >= strategyConfiguration.getExitThresholdHigh()){
                    strategyService.deactivateStrategy(strategyConfiguration.getId());
                    System.out.println("REMOVED: " + strategyConfiguration.getStrategyName() + " hit the high exit threshold at " + percentageGoL + "%");
                    return false;
                }
                else{
                    if(percentageGoL <= (-1 * strategyConfiguration.getExitThresholdLow())){
                        strategyService.deactivateStrategy(strategyConfiguration.getId());
                        System.out.println("REMOVED: " + strategyConfiguration.getStrategyName() + " hit the low exit threshold at " + percentageGoL + "%");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Map<Integer, TMA> getActiveTMAStrategies() {
        return activeTMAStrategies;
    }


}
