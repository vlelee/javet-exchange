package com.citi.exchange.services;

import com.citi.exchange.algorithms.Strategy;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.repos.StrategyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import static jdk.nashorn.internal.objects.NativeMath.round;

@Service
@Transactional (propagation = Propagation.REQUIRED)
public class StrategyService {
    @Autowired
    private StrategyRepo repo;

    public Collection<StrategyConfiguration> getStrategies() {
        return makeCollection(repo.findAll());
    }

    public Collection<StrategyConfiguration> getActiveStrategies() {
        return makeCollection(repo.findAllActive());
    }

    public StrategyConfiguration getStrategyById(int id) {
        return repo.findById(id).get();
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void addNewStrategy(StrategyConfiguration strat){
        repo.save(strat);
    }

    @Transactional()
    public void updateStrategy(StrategyConfiguration newStrategy, int id){
        StrategyConfiguration updated_strategy = getStrategyById(id);
        updated_strategy.setStrategyName(newStrategy.getStrategyName());
        repo.save(updated_strategy);
    }
    @Transactional()
    public void deactivateStrategy(int id ){
        StrategyConfiguration strat = getStrategyById(id);
        strat.setActive(false);
        repo.saveAndFlush(strat);
    }

    private static Collection<StrategyConfiguration> makeCollection(Iterable<StrategyConfiguration> iter) {
        Collection<StrategyConfiguration> list = new ArrayList<StrategyConfiguration>();
        for (StrategyConfiguration item : iter) {
            list.add(item);
        }
        return list;
    }

    public String getStrategyProfitString(int id) {
        StrategyConfiguration strategy = getStrategyById(id);
        double profit = strategy.currentPnL();
        double investmentValue = strategy.currentInvestmentValue();
        double profitPerc = (profit / investmentValue * 100);
        return ((profitPerc > 0) ? "+" : "") + ((double) Math.round(profitPerc * 100) / 100) + "% ($" + ((double) Math.round(profit * 100) / 100) + ")";
    }


    public String getStrategyNextPositionString(int id) {
        StrategyConfiguration strategy = getStrategyById(id);
        if(strategy.isBuyingAdvanced()) {
            int approximate_share_count = (int) Math.floor(strategy.currentInvestmentValue() / strategy.getInitiationPrice());
            return "Buying ~" + approximate_share_count + " shares";
        } else {
            return "Selling " + strategy.getStockHeld() + " shares";
        }
    }
}
