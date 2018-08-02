package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

	@RequestMapping(method = RequestMethod.GET, value="/active")
	Iterable<StrategyConfiguration> findAllActive() {
		return service.getActiveStrategies();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/profit")
	String getStrategyProfit(@PathVariable("id") int id) {
		return service.getStrategyProfitString(id);
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}/position")
	String getStrategyNextPosition(@PathVariable("id") int id) {
		return service.getStrategyNextPositionString(id);
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value="/{id}/trade_evals")
	List<Double> getPostTradeEvaluations(@PathVariable("id") int id) {
		return service.getStrategyById(id).getPostTradeInvestVals();
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value="/{id}/trades")
	List<Trade> getTrades(@PathVariable("id") int id) {
		return service.getStrategyById(id).getTrades();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	StrategyConfiguration getStrategyById(@PathVariable("id") int id) {
		return service.getStrategyById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	void addStrategy(@RequestBody StrategyConfiguration strat) {
		service.addNewStrategy(strat);
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	void updateStrategy(@RequestBody StrategyConfiguration strat, @PathVariable("id") int id) {
		service.updateStrategy(strat, id);
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.PUT, value="/{id}/deactivate")
	void deactivateStrategy(@PathVariable("id") int id) {
		service.deactivateStrategy(id);
	}

}

