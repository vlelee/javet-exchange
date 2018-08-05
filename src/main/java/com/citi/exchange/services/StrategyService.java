package com.citi.exchange.services;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.jms.TradeExecution;
import com.citi.exchange.repos.StrategyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StrategyService {
    @Autowired
    private StrategyRepo repo;
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeExecution tradeExecution;

    private static Collection<StrategyConfiguration> makeCollection(Iterable<StrategyConfiguration> iter) {
        Collection<StrategyConfiguration> list = new ArrayList<StrategyConfiguration>();
        for (StrategyConfiguration item : iter) {
            list.add(item);
        }
        return list;
    }

    public Collection<StrategyConfiguration> getStrategies() {
        return makeCollection(repo.findAll());
    }

    public Collection<StrategyConfiguration> getActiveStrategies() {
        return makeCollection(repo.findAllActive());
    }

    public StrategyConfiguration getStrategyById(int id) {
        return repo.findById(id).get();
    }

    public void addNewStrategy(StrategyConfiguration strat) {
        String stockTicker = strat.getStock().getTicker();
        if (stockService.isTickerPresent(stockTicker)) {
            Stock stock = stockService.getStockByTicker(stockTicker);
            strat.setStock(stock);
        }

        strat = repo.save(strat);
        Stock stock = stockService.getStockByTicker(stockTicker);
        Trade newTrade = tradeService.addNewTrade(
                new Trade(
                        strat.isInitiallyBuying(),
                        strat.getNumShares(),
                        strat.getInitiationPrice(),
                        stock, strat));
        tradeExecution.send(newTrade);
    }

    @Transactional()
    public void updateStrategy(StrategyConfiguration newStrategy, int id) {
        StrategyConfiguration updated_strategy = getStrategyById(id);
        updated_strategy.setStrategyName(newStrategy.getStrategyName());
        updated_strategy.setExitThresholdLow(newStrategy.getExitThresholdLow());
        updated_strategy.setExitThresholdHigh(newStrategy.getExitThresholdHigh());
        repo.save(updated_strategy);
    }

    @Transactional()
    public void deactivateStrategy(int id) {
        StrategyConfiguration strat = getStrategyById(id);
        strat.setActive(false);
        repo.saveAndFlush(strat);
    }

    /**
     * Using same logic as currentInvestmentValue, returns the value of the original investment plus the profit earned by the strategy after each trade
     * @return a List of the Investment Values after each trade
     */
    public List<Double> getPostTradeInvestVals(int id) {
        StrategyConfiguration strat = getStrategyById(id);
        List<Double> trade_values = new ArrayList<Double>();
        double investmentValue = strat.getNumShares() * strat.getInitiationPrice();
        double initialCash = (strat.isInitiallyBuying()) ? investmentValue : 0;
        double currentCash = initialCash;
        double lastTrade = 0;
        trade_values.add(investmentValue);

        for (Trade trade : strat.getTrades()) {
            double currentTrade = trade.getTradePrice() * trade.getNumShares();
            currentCash += (trade.isBuying() ? -1 : 1) * currentTrade;

            if (trade.isBuying() != strat.isInitiallyBuying()) {
                investmentValue += (trade.isBuying()) ? (currentCash - initialCash) : (lastTrade - currentTrade);
            } else {
                lastTrade = trade.getTradePrice() * trade.getNumShares();
            }
            trade_values.add(investmentValue);
        }
        return trade_values;
    }
}
