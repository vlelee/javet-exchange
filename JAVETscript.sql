 drop database javet;

CREATE DATABASE IF NOT EXISTS javet;
use javet;

create table stocks (
ticker varchar(10) primary key unique,
stock_name varchar(50) not null
);

create table strategy_configurations(
id int primary key auto_increment,
strategy_name varchar(30) unique not null,
algo enum('TMA', 'BB','PB') not null,
stock varchar(10) not null,
start_time timestamp not null,
end_time timestamp null default null ,
initiation_price double not null,
exit_price double,
num_shares int not null,
buying boolean not null,
active boolean not null,
exit_threshold_high double not null,
exit_threshold_low double not null,
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
strategy int not null,
buying boolean not null,
num_shares int not null, 
trade_price double not null,
time_traded timestamp default current_timestamp,
response_message varchar(200),
FOREIGN KEY (stock) REFERENCES stocks(ticker) on delete restrict,
FOREIGN KEY (strategy) REFERENCES strategy_configurations(id) on delete restrict
);


insert into stocks values ("AAPL", "Apple Inc.");
insert into stocks values ("GOOGL", "Alphabet Inc.");
insert into stocks values ("C ", "Citi");
insert into stocks values ("BFY ", "Blackrock");
insert into strategy_configurations values (1, "MyStrat", 'TMA', "GOOGL", current_timestamp, null, 100.50, null, 100,  true, true, 10.5, 5.5);
insert into strategy_configurations values (2, "2ndStrat", 'BB', "GOOGL", current_timestamp, null, 100.50, null, 100,  true, false, 10.5, 5.5);

insert into strategy_configurations values (1, "AbesStrat", 'TMA', "GOOGL", current_timestamp, null, 100.50, null, 100,  true, true, 10.5, 5.5);
insert into strategy_configurations values (2, "VickisStrat", 'TMA', "AAPL", current_timestamp, null, 100.50, null, 100,  true, true, 10.5, 5.5);
