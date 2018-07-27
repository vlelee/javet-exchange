package com.citi.exchange.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "strategies")
public class Strategy implements Serializable {


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

    @OneToMany(mappedBy = "strategyStockPair", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Stock> strategyStockPairs = new ArrayList<>();

    public Strategy(String strategyName, Algo algo, Integer stockId) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.stockId = stockId;
    }

    public Strategy() {
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


}
