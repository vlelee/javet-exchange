package com.citi.exchange.controllers;

import com.citi.exchange.entities.StockPrice;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.services.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stockprices")
public class StockPriceController {
    @Autowired
    private StockPriceService service;

    @RequestMapping(method = RequestMethod.GET)
    Iterable<StockPrice> findAll() {
        return service.getStockPrices();
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    StockPrice getStockPriceById(@PathVariable("id") int id) {
        return service.getStockPriceById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    void addStockPrice(@RequestBody StockPrice aStockPrice) {
        service.addNewStockPrice(aStockPrice);
    }

}
