package com.citi.exchange.repos;

import com.citi.exchange.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepo extends JpaRepository<Trade, Integer> {
}
