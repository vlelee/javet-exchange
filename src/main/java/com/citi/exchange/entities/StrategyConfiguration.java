package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "strategy_configurations")
public class StrategyConfiguration implements Serializable {
    public enum Algo {
        TMA, BB, PB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "initiation_price")
    private Double initiationPrice;
    @Column(name = "exit_price")
    private Double exitPrice;

    @Column(name = "num_shares")
    private Integer numShares;

    @Column(name = "exit_threshold_high")
    private Double exitThresholdHigh;

    @Column(name = "exit_threshold_low")
    private Double exitThresholdLow;

    @JoinColumn(name = "stock", referencedColumnName = "ticker", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    @JsonProperty("stock")
    private Stock stock;

    @OneToMany(mappedBy = "strategy", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<Trade> trades = new ArrayList<Trade>();

    public StrategyConfiguration(String strategyName, Stock stock,Algo algo, Timestamp startTime, Double initiationPrice, Integer numShares, Double exitThresholdHigh, Double exitThresholdLow) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.startTime = startTime;
        this.initiationPrice = initiationPrice;
        this.numShares = numShares;
        this.exitThresholdHigh = exitThresholdHigh;
        this.exitThresholdLow = exitThresholdLow;
        this.stock = stock;
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

    public Double getInitiationPrice() {
        return initiationPrice;
    }

    public void setInitiationPrice(Double initiationPrice) {
        this.initiationPrice = initiationPrice;
    }

    public Double getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(Double exitPrice) {
        this.exitPrice = exitPrice;
    }

    public Integer getNumShares() {
        return numShares;
    }

    public void setNumShares(Integer numShares) {
        this.numShares = numShares;
    }

    public Double getExitThresholdHigh() {
        return exitThresholdHigh;
    }

    public void setExitThresholdHigh(Double exitThresholdHigh) {
        this.exitThresholdHigh = exitThresholdHigh;
    }

    public Double getExitThresholdLow() {
        return exitThresholdLow;
    }

    public void setExitThresholdLow(Double exitThresholdLow) {
        this.exitThresholdLow = exitThresholdLow;
    }
//
//    public List<Trade> getTrades() {
//        return trades;
//    }
//
//    public void setTrades(List<Trade> trades) {
//        this.trades = trades;
//    }
}
