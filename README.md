# JaveTrading Platform
## Abnob Doss | Vicki Lee | Julie Rosenbaum


## Description
Create automated trading strategies that are informed by live market data. The strategies decide when to execute trades based on algorithmic analysis of the market data.  The platform provides a trader the ability to deactivate and adjust an existing strategy, along with real-time performance statsof each strategy.

## Technology and Roles  
* __User Interface:__ 
 _Visualizing, creating and adding strategies and trades_
    * Abnob Doss
    * Developed in HTML5 and JQuery with Mustache 

 
* __Core Business Logic:__

    _Monitoring live market data_
	* Vicki Lee 
	* Developed in Java using Spring and a "mock" data feed server

    _Continually executing strategies based on user configurations_
	* Vicki Lee | Abnob Doss
	* Developd in Java using Spring

   _Ordering trades in the market_
	* Julie Rosenbaum
	* Developed in Java using Spring and Java Message Service (JMS)

* __Persistence:__ 
_Maintaining a database containing stock, strategy, trade, and live market data, and mapping that data to Java objects to execute strategy decisions_
    * Julie Rosenbaum 
    * Developed in Java using a Spring Data Rest app with JDBC repositories and a MySQL database

## Architecture
### Database Design
![EER Diagram of Javet Database](./EERDiagram.png)

### Application Flow
Components of the application:
* Database
* REST API
* UI
* MockFeed and MockFeed parser
* Strategy Engine
* Trade Executor

## Approach
 1. Design Database (ongoing as project progresses)
 2. Build REST API / persistence mappings using Spring Data 
 3. Build a service to retrieve live stock prices from MockYahoo market data feed
 4. Build a web interface (ongoing process as more components are developed)
 5. Build messaging system to contact OrderBroker and execute trades
 6. Build engine to start a Strategy scheduler
 7. Design first strategy algorithm Two Moving Averages (TMA)
 8. Implement algorithm with trade execution 
 9. Improve UI to populate with live information; obtain approval from client on layout and functionality
 

## Improvements

