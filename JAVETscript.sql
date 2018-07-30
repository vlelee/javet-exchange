 drop database javet;

CREATE DATABASE IF NOT EXISTS javet;
use javet;

create table stocks (
ticker varchar(10) primary key,
stock_name varchar(50) not null,
tracking boolean not null DEFAULT true
);

create table strategy_configurations(
id int primary key auto_increment,
strategy_name varchar(30) unique not null,
algo enum('TMA', 'BB','PB') not null,
stock varchar(10) not null,
start_time timestamp not null,
end_time timestamp,
initiation_price double not null,
exit_price double,
num_shares int not null,
exit_threshold_high int not null,
exit_threshold_low int not null,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict
);

CREATE TABLE stock_prices(
id int primary key auto_increment,
stock varchar(10) not null,
time_stamp timestamp not null ,
price double not null,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict
);

create table trades(
id int primary key auto_increment, 
stock varchar(10) not null,
strategy_id int not null,
selling boolean not null,
num_shares int not null, 
tradePrice double not null, 
time_traded timestamp default current_timestamp,
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict,
FOREIGN KEY (strategy_id) REFERENCES strategy_configurations(id) on delete restrict
);


insert into stocks values ("AAPL", "Apple Inc.", true);
insert into stocks values ("GOOGL", "Alphabet Inc.", true);
insert into stocks values ("C ", "Citi", true);
insert into stocks values ("BFY ", "Blackrock", false);

/*
insert into stocks values ("BFY", "Blackrock", true);
insert into stocks values ("C", "Citi", true);
insert into stocks values ("C", "Citi", true);
insert into stocks values ("C", "Citi", true);
insert into stocks values ("C", "Citi", true);
 ["AAPL", "AMZN", "C", "KO", "MSFT", "NFLX"];
*/
