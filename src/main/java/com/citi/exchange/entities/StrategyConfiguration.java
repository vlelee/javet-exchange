package com.citi.exchange.entities;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "strategy_name")
    private String strategyName;

    //see: http://tomee.apache.org/examples-trunk/jpa-enumerated/
    @Enumerated(EnumType.STRING)
    @Column(name = "algo")
    private Algo algo;

    @Column(name = "stock_id")
    private Integer stockId;


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



    @OneToMany(mappedBy = "strategyStockPair", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Stock> strategyStockPairs = new ArrayList<>();

    public StrategyConfiguration(String strategyName, Algo algo, Integer stockId, Timestamp startTime, Double openPrice, Integer numShares) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.stockId = stockId;
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

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public List<Stock> getStrategyStockPairs() {
        return strategyStockPairs;
    }

    public void setStrategyStockPairs(List<Stock> strategyStockPairs) {
        this.strategyStockPairs = strategyStockPairs;
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
