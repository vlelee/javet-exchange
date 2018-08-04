-- drop database javet;

-- CREATE DATABASE IF NOT EXISTS javet;
-- use javet;

drop table if exists stocks;
create table stocks (
ticker varchar(10) primary key unique,
stock_name varchar(50) not null
);

drop table if exists strategy_configurations;
create table strategy_configurations(
id int primary key auto_increment,
stock varchar(10) not null,
strategy_name varchar(30) unique not null,
algo enum('TMA', 'BB','PB') not null,
start_time timestamp(6) not null,
end_time timestamp(6) null default null ,
initiation_price double not null,
exit_price double,
num_shares int not null,
initially_buying boolean not null,
active boolean not null,
exit_threshold_high double not null,
exit_threshold_low double not null,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict
);

drop table if exists stock_prices;
create table stock_prices(
id int primary key auto_increment,
stock varchar(10) not null,
time_stamp timestamp(6) not null ,
price double not null,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict
);

drop table if exists trades;
create table trades(
id int primary key auto_increment,
stock varchar(10) not null,
strategy int not null,
buying boolean not null,
num_shares int not null,
trade_price double not null,
time_traded timestamp(6) default current_timestamp(6),
response_message varchar(1000),
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict,
FOREIGN KEY (strategy) REFERENCES strategy_configurations(id) on delete restrict
);


-- insert into stocks values ("AAPL", "Apple Inc.");
-- insert into stocks values ("GOOG", "Alphabet Inc.");
-- insert into stocks values ("MSFT", "Microsoft");
-- insert into stocks values ("NSC", "Norfolk Southern Corp.");
-- insert into stocks values ("BRK-A", "Berkshire Hathaway Inc., Class A");
-- insert into strategy_configurations values (1, "Julie 3rdQ MSFT", 'TMA', "GOOGL", current_timestamp, null, 44, null, 100,  true, true, 10.5, 5.5);
-- insert into strategy_configurations values (2, "2ndStrat", 'BB', "GOOGL", current_timestamp, null, 100.50, null, 100,  true, false, 10.5, 5.5);
-- insert into strategy_configurations values (3, "Abe 3rdQ GOOGL", 'TMA', "GOOGL", current_timestamp, null, 100.50, null, 100,  true, true, 10.5, 5.5);
-- insert into strategy_configurations values (4, "Vicki 3rdQ APPL", 'TMA', "AAPL", current_timestamp, null, 100.50, null, 100,  true, true, 10.5, 5.5);
