package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "stock_name")
    private String stockName;


    @OneToMany(mappedBy = "stockFromStrategy", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<StrategyConfiguration> strategyStockPairs = new ArrayList<StrategyConfiguration>();


    public Stock() {
    }
    public Stock(String ticker, String stockName) {
        this.ticker = ticker;
        this.stockName = stockName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public List<StrategyConfiguration> getStrategyStockPairs() {
        return strategyStockPairs;
    }

    public void setStrategyStockPairs(List<StrategyConfiguration> strategyStockPairs) {
        this.strategyStockPairs = strategyStockPairs;
    }


}
