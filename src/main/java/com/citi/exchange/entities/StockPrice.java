package com.citi.exchange.entities;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Timestamp;


@Entity
@Table (name = "stock_prices")
public class StockPrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id") private int id;
    @Column(name = "time_stamp") private Timestamp timestamp;

    @Column(name = "stock_id") private Integer stockId;
    @Column(name = "price") private Double price;

    public StockPrice(Timestamp timestamp, Integer stockId, Double price) {
        this.timestamp = timestamp;
        this.stockId = stockId;
        this.price = price;
    }

    public StockPrice() {
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

//    public double getAverageForPast(Integer stockId, Integer minutes) {
//        String sql = "SELECT SUM(price) / COUNT(*) FROM market_data WHERE time_stamp > DATE_SUB(NOW(), INTERVAL :minutes MINUTE)";
//        //TODO: Need a hibernate util class to call getSessionFactory()
//        SessionFactory sessionFactory = getSessionFactory();
//        Session session = sessionFactory.openSession();
//        TypedQuery<Double> sqlQuery = session.createQuery(sql);
//        sqlQuery.setParameter("minutes", minutes);
//        return sqlQuery.getResultList().get(0);
//    }
}

