package com.citi.exchange.repos;

import com.citi.exchange.entities.StrategyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StrategyRepo extends JpaRepository<StrategyConfiguration, Integer> {
    @Query("SELECT s FROM StrategyConfiguration s WHERE s.active = 1")
    Iterable<StrategyConfiguration> findAllActive();
}
