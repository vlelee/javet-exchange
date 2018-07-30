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

    @Column(name = "price") private Double price;

    @JoinColumn(name = "stock", referencedColumnName = "ticker", nullable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Stock stock;

    public StockPrice(Timestamp timestamp, Stock stock, Double price) {
        this.timestamp = timestamp;
        this.stock = stock;
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


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

