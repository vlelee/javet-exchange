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

    //@Transactional(propagation = Propagation.REQUIRES_NEW)
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

    public String getStrategyProfitString(int id) {
        StrategyConfiguration strategy = getStrategyById(id);
        double investmentValue = strategy.currentInvestmentValue();
        double initialPrice = strategy.getNumShares() * strategy.getInitiationPrice();
        double profit = strategy.currentPnL(investmentValue, initialPrice);
        double profitPerc = (profit / investmentValue * 100);
        return ((profitPerc > 0) ? "+" : "") + ((double) Math.round(profitPerc * 100) / 100) + "% ($" + ((double) Math.round(profit * 100) / 100) + ")";
    }


    public String getStrategyNextPositionString(int id) {
        StrategyConfiguration strategy = getStrategyById(id);
        if (strategy.isCurrentlyBuying()) {
            int approximate_share_count = (int) Math.floor(strategy.currentInvestmentValue() / strategy.getInitiationPrice());
            return "Buying ~" + approximate_share_count + " shares";
        } else {
            return "Selling " + strategy.getSharesCurrentlyHeld() + " shares";
        }
    }
}
