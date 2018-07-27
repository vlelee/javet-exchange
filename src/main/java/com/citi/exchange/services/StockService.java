package com.citi.exchange.services;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.repos.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional (propagation=Propagation.REQUIRED)
public class StockService {
    @Autowired
    private StockRepo repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addNewStock(Stock aStock){
        if(!getStocks().contains(aStock))
            repo.save(aStock);
    }
    public Collection<Stock> getStocks() {
        return makeCollection(repo.findAll());
    }

    public Stock getStockById(int id) {
        return repo.findById(id).get();
    }

    private static Collection<Stock> makeCollection(Iterable<Stock> iter) {
        Collection<Stock> list = new ArrayList<>();
        for (Stock item : iter) {
            list.add(item);
        }
        return list;
    }
}
