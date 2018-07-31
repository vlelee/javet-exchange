package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.web.ResourceProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker", columnDefinition = "VARCHAR(10)", nullable = false, unique = true)
    private String ticker;

    @Column(name = "stock_name")
    private String stockName;


    @OneToMany(mappedBy = "stock", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<StrategyConfiguration> strategies = new ArrayList<StrategyConfiguration>();

    @OneToMany(mappedBy = "stock", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<StockPrice> stockPrices = new ArrayList<StockPrice>();

    @OneToMany(mappedBy = "stock", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<Trade> trades = new ArrayList<Trade>();


    public Stock() {
    }

    public Stock(String ticker, String stockName) {
        this.ticker = ticker;
        this.stockName = stockName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker.trim();
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }


    public List<StrategyConfiguration> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<StrategyConfiguration> strategies) {
        this.strategies = strategies;
    }

    public List<StockPrice> getStockPrices() {
        return stockPrices;
    }

    public void setStockPrices(List<StockPrice> stockPrices) {
        this.stockPrices = stockPrices;
    }
}
