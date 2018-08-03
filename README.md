# JaveTrading Platform
### Abnob Doss | Vicki Lee | Julie Rosenbaum


## Description
Create automated trading strategies that are informed by live market data. The strategies decide when to execute trades based on algorithmic analysis of the market data.  The platform provides a trader the ability to deactivate and adjust an existing strategy, along with real-time performance statsof each strategy.


## To set environment variables for local machine
In application.properties, the variables look like this:
```
spring.datasource.url=jdbc:mysql://${DBHOST}/${DBNAME}
spring.datasource.username=${USERNAME}
spring.datasource.password=${PASSWORD}
spring.activemq.broker-url=tcp://${ACTIVEMQ}
```

In Intellij:

`Edit Run Configurations...` --> In the popup select `Spring Boot` --> `ExchangeApplication` --> `Environment` --> `Environment Variables` --> Click the plus `...` next to the empty box --> In the popup click `+` to add the 5 new variables:

Name     | Value
---------|---------------
`DBHOST`   | `localhost:3306`
`DBNAME`   | `javet`
`USERNAME` | `root`
`PASSWORD` | `c0nygre`
`ACTIVEMQ` | `localhost:61616`

Click `Ok` and then hitting the play button should work the same!


## Runbook
 * TODO: what dis

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
	* Developed in Java using Spring

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
    * stocks
    * stock_prices
    * strategy_configurations
    * trades
* REST API
    * Creates entites, repositories, services and controllers to represent the database mappings
    * Allows access to add, update and retrieve elements in the database via GET, POST and PUT requests
```
     /api
     ├── /stocks
     │   └── /ticker
     ├── /strategies
     │   ├── /active
     │   └── /id
     │       ├── /profit 
     │       ├── /deactivate 
     │       ├── /position 
     │       ├── /trade_evals
     │       └── /trades
     ├── /stockprices
     │   ├── /id
     │   └── /ticker
     │       └── /latest
     └── /trades
         └── /id   
```
* UI
    * Calls the /strategy, /stocks, and /stockprices APIs via JQuery GET requests and displays this live, as well as historic, stock and strategy data. JQuery is then used to intervally refresh these fields so that the user is always seeing the most up-to-date and live data.
    * Uses the Mustache.JS library to enable our large and dynamic layouts to be cached and rendered for increased performance.
    * Uses JQuery POST and PUT requests to the /strategy API to allow users to edit and disable live strategies.
* MockFeed and MockFeed parser
    * Creates a scheduler to continually make GET requests to the Conygre feed
    * Inputs them to the stock_prices table
* Trade Executor
    * Takes in a trade object with the specifications of the trade
    * Constructs a JMS message to send to an ActiveMQ queue
    * Listens for a response from the OrderBroker, confirming the status of the trade
* Strategy Engine
    * Comprised of a Strategy Execution engine and the individual algorithms for each strategy
    * The Execution engine takes in a list of active strategies
        * It continually makes calls to run the strategy algorithm based on its performance
        * Decides when to deactivate the strategy based on performance
        * Calculates performance analytics
    * The individual strategy algorithms handle the logic of the financial strategy
        * Takes in all active strategies of this algorithm (currently only TMA)
        * Decides when to execute trades based on the strategy configurations passed in
    

## Approach
 1. Design database (ongoing as project progresses)
 2. Build REST API / persistence mappings using Spring Data 
 3. Build a service to retrieve live stock prices from MockYahoo market data feed
 4. Build a web interface (ongoing process as more components are developed)
 5. Obtain approval from client on layout and functionality
 6. Build messaging system to contact OrderBroker and execute trades
 7. Build engine to start a Strategy scheduler
 8. Design first strategy algorithm Two Moving Averages (TMA)
 9. Implement algorithm with trade execution 
 10. Improve UI to populate with live information
 11. Debug and add missing features to achieve minimum viable product
 12. Push Docker image to production (ongoing communication with Production Support team)
 13. Retroactively create and run tests
 14. Complete stretch goals ...
 

## Improvements
Tech/Development Procedures
* Testing
    * Do Test Driven Development! We attempted to start with this, but it was time consuming so we shelved the process in the interest of time and completing a fully functional minimum viable product
    * Behavior Driven Development / Acceptance Testing for the front end
* More Code Review to avoid duplicating functionality and making workflow more streamlined
* Add documentation to the code itself to make clear each class/function's purpose and use case


Additional Features
* Implement the other two strategies: Bollinger Bands and Price Breakout
* Add Historical Stock Data
* Searching through strategies
* Ability to re-activate a strategy with the same configurations


