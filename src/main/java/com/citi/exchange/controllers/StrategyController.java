package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import com.citi.exchange.entities.StrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

	@Autowired
	private com.citi.exchange.services.StrategyService service;

	@RequestMapping(method = RequestMethod.GET)
	Iterable<StrategyConfiguration> findAll() {
		return service.getStrategyStockPairings();
	}


}
