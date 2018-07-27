drop database javet;

CREATE DATABASE IF NOT EXISTS javet;
use javet;

create table stocks (
id int primary key auto_increment,
ticker varchar(10) not null,
stock_name varchar(50) not null

);

create table strategies(
id int primary key auto_increment,
strategy_name varchar(30) not null,
algo enum('TMA', 'BB','PB') not null,
stock_id int not null,
num_shares int not null,
exit_position int not null,
start_time timestamp not null, 
end_time timestamp, 
open_price double not null,
close_price double,
FOREIGN KEY (stock_id) REFERENCES stocks(id) on delete restrict
);

CREATE TABLE market_data(
id int primary key auto_increment,
stock_id int not null,
time_stamp timestamp not null ,
price double not null,
FOREIGN KEY (stock_id) REFERENCES stocks(id) on delete restrict
);


insert into stocks values (1, "AAPL", "Apple");
insert into stocks values (2, "GOOGL", "Alphabet, Inc.");
insert into stocks values (3, "BFY", "Blackrock");
insert into stocks values (4, "C", "Citi");

/*
insert into strategies values (0,"AAPL 3rd Q", 'TMA', 1);
insert into strategies values (3, "BFY 3rd Q", 'BB', 3);
insert into strategies values (6,"GOOGL 3rd Q", 'PB', 2);
*/
