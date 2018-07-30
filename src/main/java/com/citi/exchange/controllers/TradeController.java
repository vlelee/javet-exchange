package com.citi.exchange.controllers;

import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.entities.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

	@Autowired
	private com.citi.exchange.services.TradeService service;

	@RequestMapping(method = RequestMethod.GET)
	Iterable<Trade> findAll() {
		return service.getTrades();
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	Trade getTradeById(@PathVariable("id") int id) {
		return service.getTradeById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	void addTrade(@RequestBody Trade trade) {
		service.addNewTrade(trade);
	}


}

