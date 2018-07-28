package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DashboardController {

	@Autowired
	private com.citi.exchange.services.StockService service;


	@RequestMapping(method = RequestMethod.GET)
	Iterable<Stock> findAll() {
		return service.getStocks();
	}
	//	public String index() {
//
//		Stock s = new Stock("FB","Facebook, Inc." );
//		return s.getStockName();
//
//
//
//	}


}
