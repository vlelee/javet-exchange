package com.citi.exchange.controllers;

import com.citi.exchange.entities.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

	@RequestMapping("/")
	public String index() {

		Stock s = new Stock("FB","Facebook, Inc." );
		return s.getStockName();



	}


}
