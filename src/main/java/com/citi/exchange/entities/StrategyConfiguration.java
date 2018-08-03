package com.citi.exchange.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "strategy_name")
    private String strategyName;

    //see: http://tomee.apache.org/examples-trunk/jpa-enumerated/
    public enum Algo {
        TMA, BB, PB
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "algo")
    private Algo algo;

    //TODO: Never used - delete from DB and entity
    @Column(name = "start_time")
    private java.sql.Timestamp startTime;

    //TODO: Never used - delete from DB and entity
    @Column(name = "end_time")
    private java.sql.Timestamp endTime;

    @Column(name = "initiation_price")
    private Double initiationPrice;

    //TODO: Never used - delete from DB and entity
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
    @ManyToOne(cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @JsonProperty("stock")
    private Stock stock;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Trade> trades = new ArrayList<Trade>();

    /*
    Constructors
     */
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

    /*
    Below: Getters and setters for entity properties
     */
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

    //TODO: Never used - delete from DB and entity
    public Timestamp getStartTime() {
        return startTime;
    }

    //TODO: Never used - delete from DB and entity
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    //TODO: Never used - delete from DB and entity
    public Timestamp getEndTime() {
        return endTime;
    }

    //TODO: Never used - delete from DB and entity
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Double getInitiationPrice() {
        return initiationPrice;
    }

    public void setInitiationPrice(Double initiationPrice) {
        this.initiationPrice = initiationPrice;
    }

    //TODO: Never used - delete from DB and entity
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

    public boolean isInitiallyBuying() {
        return buying;
    }

    public void setInitiallyBuying(boolean buying) {
        this.buying = buying;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    /*
    Below: Methods for calculations in strategy performance analysis
     */

    /**
     * If the strategy has executed trades, returns whether or not the strategy is in an open (sell next) or closed (buy next) position
     * @return  True if strategy buying, False if strategy selling
     */
    public boolean isCurrentlyBuying() {
        if (getTrades().size() == 0) {
            return buying;
        } else {
            return (getSharesCurrentlyHeld() == 0);
        }
    }

    /**
     * Iterates through the trades executed by the strategy and calculates the number of shares currently left
     * @return number of shares currently being traded by the strategy
     */
    public int getSharesCurrentlyHeld() {
        int noSharesHeld = 0;
        for (Trade trade : trades) {
            noSharesHeld += (trade.isBuying() ? 1 : -1) * trade.getNumShares();
        }
        return Math.abs(noSharesHeld);
    }

    /**
     * Calculates investment value (#shares * initial share price) and cash on hand based on the trade execution history
     * @return value of original investment + profit earned  by strategy so far
     */
    public double currentInvestmentValue() {
        double investmentValue = getNumShares() * getInitiationPrice();
        double initialCash = (isInitiallyBuying()) ? investmentValue : 0;
        double currentCash = initialCash;
        double lastTrade = 0;

        for (Trade trade : getTrades()) {
            double currentTrade = trade.getTradePrice() * trade.getNumShares();
            currentCash += (trade.isBuying() ? -1 : 1) * currentTrade;

            if (trade.isBuying() != isInitiallyBuying()) {
                investmentValue += (trade.isBuying()) ? (currentCash - initialCash) : (lastTrade - currentTrade);
            } else {
                lastTrade = trade.getTradePrice() * trade.getNumShares();
            }
        }
        return investmentValue;
    }

    /**
     * Using same logic as currentInvestmentValue, returns the value of the original investment plus the profit earned by the strategy after each trade
     * @return a List of the Investment Values after each trade
     */
    public List<Double> getPostTradeInvestVals() {
        List<Double> trade_values = new ArrayList<Double>();
        double investmentValue = getNumShares() * getInitiationPrice();
        double initialCash = (isInitiallyBuying()) ? investmentValue : 0;
        double currentCash = initialCash;
        double lastTrade = 0;
        trade_values.add(investmentValue);

        for (Trade trade : getTrades()) {
            double currentTrade = trade.getTradePrice() * trade.getNumShares();
            currentCash += (trade.isBuying() ? -1 : 1) * currentTrade;

            if (trade.isBuying() != isInitiallyBuying()) {
                investmentValue += (trade.isBuying()) ? (currentCash - initialCash) : (lastTrade - currentTrade);
            } else {
                lastTrade = trade.getTradePrice() * trade.getNumShares();
            }
            trade_values.add(investmentValue);
        }
        return trade_values;
    }

    /**
     * returns current Investment Value - NumShares * Initial Stock Price;
     * @param currentInvestVal total value of investment = original investment + profit
     * @param initialPrice the price per share when strategy was initiated
     * @return the current profit or loss on the total investment
     */
    public double currentPnL(double currentInvestVal, double initialPrice) {
        return currentInvestVal - initialPrice;
    }


    /**
     * returns the % gain or loss on the investment
     * @param currentPnL current profit or loss on total investment
     * @param currentInvVal total value of investment  = original investment + profit
     * @return the percentage of profit or loss from current investment value
     */
    public double getGainOrLossFromPNL(double currentPnL, double currentInvVal) {
        if (currentInvVal == 0.0)
            return 0.0;

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        return Double.parseDouble(df.format((currentPnL / currentInvVal) * 100));
    }
}