package com.citi.exchange.services;

import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.repos.StrategyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional (propagation = Propagation.REQUIRED)
public class StrategyService {
    @Autowired
    private StrategyRepo repo;

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void addNewStrategy(StrategyConfiguration strat){
        repo.save(strat);
    }
//    public Collection<StrategyConfiguration> getPairings() {
//        return makeCollection(repo.findAll());
//    }
//
//    public StrategyConfiguration getStrategyById(int id) {
//        return repo.findOne();
//
//    private static Collection<StrategyConfiguration> makeCollection(Iterable<StrategyConfiguration> iter) {
//        Collection<StrategyConfiguration> list = new ArrayList<StrategyConfiguration>();
//        for (StrategyConfiguration item : iter) {
//            list.add(item);
//        }
//        return list;
//    }
//    }
}
