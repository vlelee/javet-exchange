package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// TODO: Deccide if we'd like to remove cross-origin. This in place to connect it to the frontend at the moment.
@CrossOrigin
@RestController
@RequestMapping("/api/stocks")
public class StockController {

	@Autowired
	private com.citi.exchange.services.StockService service;

	@RequestMapping(method = RequestMethod.GET)
	Iterable<Stock> findAll() {
		return service.getStocks();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{ticker}")
	Stock getStockByTicker(@PathVariable("ticker") String ticker) {
		return service.getStockByTicker(ticker);
	}

	@RequestMapping(method = RequestMethod.POST)
	void addStock(@RequestBody Stock stock) {
		service. addNewStock(stock);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{ticker}")
	void addStock(@RequestBody Stock stock, @PathVariable("ticker") String ticker) {
		service.updateStock(stock, ticker);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{ticker}/hide")
	void hideStock(@PathVariable("ticker") String ticker) {
		service.toggleStockVisbility(ticker, false);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{ticker}/show")
	void showStock(@PathVariable("ticker") String ticker) {
		service.toggleStockVisbility(ticker, true);
	}

}
