package com.citi.exchange.repos;

import com.citi.exchange.entities.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StockPriceRepo extends JpaRepository<StockPrice, Integer> {

}
