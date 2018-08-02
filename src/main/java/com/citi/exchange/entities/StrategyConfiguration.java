package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean buying;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Column(name = "exit_threshold_high")
    private Double exitThresholdHigh;

    @Column(name = "exit_threshold_low")
    private Double exitThresholdLow;

    @JoinColumn(name = "stock", referencedColumnName = "ticker", nullable = false)
    @ManyToOne(cascade=CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @JsonProperty("stock")
    private Stock stock;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Trade> trades = new ArrayList<Trade>();

    public StrategyConfiguration(String strategyName, Algo algo, Timestamp startTime, Double initiationPrice, Integer numShares, boolean buying, boolean active, Double exitThresholdHigh, Double exitThresholdLow, Stock stock) {
        this.strategyName = strategyName;
        this.algo = algo;
        this.startTime = startTime;
        this.initiationPrice = initiationPrice;
        this.numShares = numShares;
        this.buying = buying;
        this.active = active;
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

    public boolean isBuying() {
        return buying;
    }

    public boolean isBuyingAdvanced() {
        if (getTrades().size() == 0) {
            return buying;
        } else {
            return (getStockHeld() == 0);
        }
    }

    public int getStockHeld() {
        int noSharesHeld = 0;
        for(Trade trade : trades) {
            noSharesHeld += (trade.isBuying() ? 1 : -1) * trade.getNumShares();
        }

        return Math.abs(noSharesHeld);
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
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

    public double currentInvestmentValue() {
        double investmentValue = getNumShares() * getInitiationPrice();
        double initialCash = (isBuying()) ? investmentValue : 0;
        double currentCash = initialCash;
        double lastTrade = 0;

        for(Trade trade : getTrades()) {
            double currentTrade = trade.getTradePrice() * trade.getNumShares();
            currentCash += (trade.isBuying() ? -1 : 1) * currentTrade;

            if(trade.isBuying() != isBuying()){
                investmentValue += (trade.isBuying()) ? (currentCash - initialCash) : (lastTrade - currentTrade);
            } else {
                lastTrade = trade.getTradePrice() * trade.getNumShares();
            }
        }
        return investmentValue;
    }

    public List<Double> getPostTradeInvestVals() {
        List<Double> trade_values = new ArrayList<Double>();
        double investmentValue = getNumShares() * getInitiationPrice();
        double initialCash = (isBuying()) ? investmentValue : 0;
        double currentCash = initialCash;
        double lastTrade = 0;
        trade_values.add(investmentValue);

        for(Trade trade : getTrades()) {
            double currentTrade = trade.getTradePrice() * trade.getNumShares();
            currentCash += (trade.isBuying() ? -1 : 1) * currentTrade;

            if(trade.isBuying() != isBuying()){
                investmentValue += (trade.isBuying()) ? (currentCash - initialCash) : (lastTrade - currentTrade);
            } else {
                lastTrade = trade.getTradePrice() * trade.getNumShares();
            }
            trade_values.add(investmentValue);
        }
        return trade_values;
    }


    public double currentPnL() {
        return currentInvestmentValue() - (getNumShares() * getInitiationPrice());
    }

    //Compute the percentage of profit or loss from current investment value
    public double getGainOrLossFromPNL(double currentPnL, double currentInvVal){
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        return Double.parseDouble(df.format((currentPnL / currentInvVal) * 100));
    }
}
