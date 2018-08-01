package com.citi.exchange.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "trades")
public class Trade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean buying;

    @Column(name = "num_shares")
    private Integer numShares;

    @Column(name = "trade_price")
    private Double tradePrice;

    @Column(name = "time_traded")
    private java.sql.Timestamp timeTraded;

    @Column(name = "response_message")
    private String responseMessage;

    @JoinColumn(name = "stock", referencedColumnName = "ticker", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Stock stock;

    @JoinColumn(name = "strategy", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private StrategyConfiguration strategy;

    public Trade() {
    }

    public Trade(boolean buying, Integer numShares, Double tradePrice, Stock stock, StrategyConfiguration strategy) {
        this.buying = buying;
        this.numShares = numShares;
        this.tradePrice = tradePrice;
        this.stock = stock;
        this.strategy = strategy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    public Integer getNumShares() {
        return numShares;
    }

    public void setNumShares(Integer numShares) {
        this.numShares = numShares;
    }

    public Timestamp getTimeTraded() {
        return timeTraded;
    }

    public void setTimeTraded(Timestamp timeTraded) {
        this.timeTraded = timeTraded;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StrategyConfiguration getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyConfiguration strategy) {
        this.strategy = strategy;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
