package com.citi.exchange.services;

import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.repos.StrategyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

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
    private static Collection<StrategyConfiguration> makeCollection(Iterable<StrategyConfiguration> iter) {
        Collection<StrategyConfiguration> list = new ArrayList<StrategyConfiguration>();
        for (StrategyConfiguration item : iter) {
            list.add(item);
        }
        return list;
    }



}
