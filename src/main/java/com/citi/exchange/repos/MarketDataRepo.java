package com.citi.exchange.repos;

import com.citi.exchange.entities.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MarketDataRepo extends JpaRepository<MarketData, Integer> {

    public Iterable<MarketData> findByStockId(@Param("stock_id") int id);

}
