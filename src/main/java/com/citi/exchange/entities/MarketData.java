package com.citi.exchange.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table (name = "market_data")
public class MarketData  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id") private int id;
    @Column(name = "time_stamp") private Timestamp timestamp;

    @Column(name = "stock_id") private Integer stockId;
    @Column(name = "price") private Double price;

    public MarketData(Timestamp timestamp, Integer stockId, Double price) {
        this.timestamp = timestamp;
        this.stockId = stockId;
        this.price = price;
    }

    public MarketData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}



