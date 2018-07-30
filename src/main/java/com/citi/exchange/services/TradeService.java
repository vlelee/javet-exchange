package com.citi.exchange.services;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.Trade;
import com.citi.exchange.repos.StockRepo;
import com.citi.exchange.repos.TradeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional (propagation=Propagation.REQUIRED)
public class TradeService {
    @Autowired
    private TradeRepo repo;

    public Collection<Trade> getTrades() {
        return makeCollection(repo.findAll());
    }

    public Trade getTradeById(Integer id) {
        return repo.findById(id).get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Trade addNewTrade(Trade aTrade){
        return repo.save(aTrade);
    }

    private static Collection<Trade> makeCollection(Iterable<Trade> iter) {
        Collection<Trade> list = new ArrayList<>();
        for (Trade item : iter) {
            list.add(item);
        }
        return list;
    }
}
