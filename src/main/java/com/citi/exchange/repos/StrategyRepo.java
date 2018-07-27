package com.citi.exchange.repos;

import com.citi.exchange.entities.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrategyRepo extends JpaRepository<Strategy, Integer> {
}
