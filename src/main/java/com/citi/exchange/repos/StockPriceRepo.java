package com.citi.exchange.repos;

import com.citi.exchange.entities.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockPriceRepo extends JpaRepository<StockPrice, Integer> {
    @Query(value = "Select s.price from stock_prices s where s.stock = ?1 order by s.time_stamp desc limit 1", nativeQuery = true)
    String findLastByTicker(String ticker);
}


/*

CREATE TABLE stock_prices(
id int primary key auto_increment,
stock varchar(10) not null,
time_stamp timestamp not null ,
price double not null,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict
);


 */