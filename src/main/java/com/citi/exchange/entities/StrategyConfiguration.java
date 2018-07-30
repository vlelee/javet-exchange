package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Integer exitThresholdHigh;

    @Column(name = "exit_threshold_low")
    private Integer exitThresholdLow;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock", referencedColumnName = "ticker") //, nullable = false)
    @JsonProperty("stock")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Stock stock;

    public StrategyConfiguration(String strategyName, Algo algo, Timestamp startTime, Double initiationPrice, Integer numShares) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.startTime = startTime;
        this.initiationPrice = initiationPrice;
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

    public Integer getExitThresholdHigh() {
        return exitThresholdHigh;
    }

    public void setExitThresholdHigh(Integer exitThresholdHigh) {
        this.exitThresholdHigh = exitThresholdHigh;
    }

    public Integer getExitThresholdLow() {
        return exitThresholdLow;
    }

    public void setExitThresholdLow(Integer exitThresholdLow) {
        this.exitThresholdLow = exitThresholdLow;
    }
}
