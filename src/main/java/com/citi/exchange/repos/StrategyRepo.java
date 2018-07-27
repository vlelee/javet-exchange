package com.citi.exchange.repos;

import com.citi.exchange.entities.StrategyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrategyRepo extends JpaRepository<StrategyConfiguration, Integer> {
}
