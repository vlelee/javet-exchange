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
    private boolean printedOnce = false;

    @Autowired
    private StockPriceWebService stockPriceWebService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private BeanFactory beanFactory;

    @Scheduled(fixedRate = 30)
    public void execute(){
        Map<String, Double> marketPrice = stockPriceWebService.getMarketPrice();
        searchForConfigurations();
//        for(TMA strategy : activeTMAStrategies.values()){
//            String ticker = strategy.getStrategyConfiguration().getStock().getTicker();
//            if(ticker != null && marketPrice.containsKey(ticker)) {
//                double currentPrice = marketPrice.get(ticker);
//                strategy.run(currentPrice);
//            }
//        }
        Iterator<TMA> iterator = activeTMAStrategies.values().iterator();
        while(iterator.hasNext()) {
            TMA strategy = iterator.next();
            if(strategy.getStrategyConfiguration().isActive()){
                String ticker = strategy.getStrategyConfiguration().getStock().getTicker();
                if(ticker != null && marketPrice.containsKey(ticker)) {
                    double currentPrice = marketPrice.get(ticker);
                    strategy.run(currentPrice);
                    isAnActiveStrategy(strategy.getStrategyConfiguration().getId());
                }
            }  else {
                iterator.remove();
            }
        }
        if(activeTMAStrategies.isEmpty() && !printedOnce){
            System.out.println("No active strategies currently");
            printedOnce = true;
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
                    }
            }

        }

    }

    public boolean isAnActiveStrategy(int strategyId){
        StrategyConfiguration strategyConfiguration = strategyService.getStrategyById(strategyId);
        double investmentVal = strategyConfiguration.currentInvestmentValue();
        double currentPNL = strategyConfiguration.currentPnL();
        double percentageGoL = strategyConfiguration.getGainOrLossFromPNL(currentPNL, investmentVal);

        if(!strategyConfiguration.isActive()){
            strategyService.deactivateStrategy(strategyId);
            return false;
        }

        if(activeTMAStrategies.containsKey(strategyConfiguration.getId())){
            if(percentageGoL >= strategyConfiguration.getExitThresholdHigh()){
                strategyService.deactivateStrategy(strategyId);
                System.out.println("REMOVED Strategy name" + strategyConfiguration.getStrategyName() + " is higher than the high exit threshold at "
                        + percentageGoL + " Active: " + strategyConfiguration.isActive());
                return false;
            }
            else if(percentageGoL <= (-1 * strategyConfiguration.getExitThresholdLow())){
                strategyService.deactivateStrategy(strategyId);
                System.out.println("REMOVED Strategy name" + strategyConfiguration.getStrategyName() + " is below the low exit threshold at "
                        + percentageGoL + " Active: " + strategyConfiguration.isActive());
                return false;
            }
        }

        return true;
    }


}
