package com.citi.exchange.repos;

import com.citi.exchange.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<Stock, String> {
}
