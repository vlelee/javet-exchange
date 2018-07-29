package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock implements Serializable {
    @Id
    @Column(name = "ticker")
    private String ticker;

    @Column(name = "stock_name")
    private String stockName;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean tracking;

/*
    @OneToMany(mappedBy = "stockFromStrategy", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<StrategyConfiguration> strategyStockPairs = new ArrayList<StrategyConfiguration>();
*/

    public Stock() {
    }
    public Stock(String ticker, String stockName, boolean tracking) {
        this.ticker = ticker;
        this.stockName = stockName;
        this.tracking = tracking;
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

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    /*
    public List<StrategyConfiguration> getStrategyStockPairs() {
        return strategyStockPairs;
    }

    public void setStrategyStockPairs(List<StrategyConfiguration> strategyStockPairs) {
        this.strategyStockPairs = strategyStockPairs;
    }*/

}
