package com.citi.exchange.services;

import com.citi.exchange.entities.Stock;

import com.citi.exchange.entities.StockPrice;
import com.citi.exchange.repos.StockPriceRepo;
import com.citi.exchange.repos.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional (propagation=Propagation.REQUIRED)
public class StockPriceService {
    @Autowired
    private StockPriceRepo repo;


    public Collection<StockPrice> getStockPrices() {
        return makeCollection(repo.findAll());
    }

    public StockPrice getStockPriceById(int id) {
        return repo.findById(id).get();
    }

    public String getLastStockPriceByTicker(String ticker) {
        return  repo.findLastByTicker(ticker);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addNewStockPrice(StockPrice aStockPrice){

        repo.save(aStockPrice);
    }

    private static Collection<StockPrice> makeCollection(Iterable<StockPrice> iter) {
        Collection<StockPrice> list = new ArrayList<>();
        for (StockPrice item : iter) {
            list.add(item);
        }
        return list;
    }

}
