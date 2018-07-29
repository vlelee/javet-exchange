package com.citi.exchange.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "strategy_configurations")
public class StrategyConfiguration implements Serializable {
    public enum Algo {
        TMA, BB, PB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "strategy_name")
    private String strategyName;

    //see: http://tomee.apache.org/examples-trunk/jpa-enumerated/
    @Enumerated(EnumType.STRING)
    @Column(name = "algo")
    private Algo algo;

    @Column(name = "start_time")
    private java.sql.Timestamp startTime;
    @Column(name = "end_time")
    private java.sql.Timestamp endTime;

    @Column(name = "open_price")
    private Double openPrice;
    @Column(name = "close_price")
    private Double closePrice;

    @Column(name = "num_shares")
    private Integer numShares;

    @Column(name = "exit_position")
    private Integer exitPosition;


    @JoinColumn(name = "stock_id", referencedColumnName = "ticker", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Stock stockFromStrategy;

    public StrategyConfiguration(String strategyName, Algo algo, Timestamp startTime, Double openPrice, Integer numShares) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.startTime = startTime;
        this.openPrice = openPrice;
        this.numShares = numShares;
    }

    public StrategyConfiguration() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Algo getAlgo() {
        return algo;
    }

    public void setAlgo(Algo algo) {
        this.algo = algo;
    }

    public Stock getStockFromStrategy() {
        return stockFromStrategy;
    }

    public void setStockFromStrategy(Stock stockFromStrategy) {
        this.stockFromStrategy = stockFromStrategy;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Integer getNumShares() {
        return numShares;
    }

    public void setNumShares(Integer numShares) {
        this.numShares = numShares;
    }
    public Integer getExitPosition() {
        return exitPosition;
    }

    public void setExitPosition(Integer exitPosition) {
        this.exitPosition = exitPosition;
    }
}
