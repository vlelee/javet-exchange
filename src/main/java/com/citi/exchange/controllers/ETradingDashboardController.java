package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ETradingDashboardController {

	@RequestMapping("/")
	public String index() {

		Stock s = new Stock("FB","Facebook, Inc." );
		return s.getStockName();
	}


}
