package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/strategies")
public class StrategyController {

	@Autowired
	private com.citi.exchange.services.StrategyService service;
	private com.citi.exchange.services.StockService stockService;

	@RequestMapping(method = RequestMethod.GET)
	Iterable<StrategyConfiguration> findAll() {
		return service.getStrategies();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	StrategyConfiguration getStrategyById(@PathVariable("id") int id) {
		return service.getStrategyById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	void addStrategy(@RequestBody StrategyConfiguration strat) {
		//strat.setStock(stockService.getStockByTicker(strat));
		service.addNewStrategy(strat);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	void addStrategy(@RequestBody StrategyConfiguration strat, @PathVariable("id") int id) {
		service.updateStrategy(strat, id);
	}

}

