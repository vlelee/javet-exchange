package com.citi.exchange.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "strategies")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "stock_name")
    private String stockName;

    @JoinColumn(name = "strategy_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Strategy strategyStockPair;

    public Stock() {
    }
    public Stock(String ticker, String stockName) {
        this.ticker = ticker;
        this.stockName = stockName;
    }
    public Stock(String ticker, String stockName, Strategy strategyStockPair) {
        this.ticker = ticker;
        this.stockName = stockName;
        this.strategyStockPair = strategyStockPair;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Strategy getStrategyStockPair() {
        return strategyStockPair;
    }

    public void setStrategyStockPair(Strategy strategyStockPair) {
        this.strategyStockPair = strategyStockPair;
    }

}
