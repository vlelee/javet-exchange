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

    public Collection<Stock> getStocks() {
        return makeCollection(repo.findAll());
    }

    public Stock getStockByTicker(String ticker) {
        return repo.findById(ticker).get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Stock addNewStock(Stock aStock){
        return repo.save(aStock);
    }

    @Transactional()
    public void updateStock(Stock aStock, String ticker){
        Stock update_stock = getStockByTicker(ticker);
        update_stock.setStockName(aStock.getStockName());
        update_stock.setTracking(aStock.isTracking());
        repo.save(update_stock);
    }

    private static Collection<Stock> makeCollection(Iterable<Stock> iter) {
        Collection<Stock> list = new ArrayList<>();
        for (Stock item : iter) {
            list.add(item);
        }
        return list;
    }

    public void toggleStockVisbility(String ticker, boolean visibility) {
        Stock update_stock = getStockByTicker(ticker);
        update_stock.setTracking(visibility);
        repo.save(update_stock);
    }
}
