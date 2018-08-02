# JaveTrading Platform
## Abnob Doss | Vicki Lee | Julie Rosenbaum


## Description
Create automated trading strategies that are informed by live market data. The strategies decide when to execute trades based on algorithmic analysis of the market data.  The platform provides a trader the ability to deactivate and adjust an existing strategy, along with real-time performance statsof each strategy.

## Technology and Roles  
* __User Interface:__ 
 Visualizing, creating and adding strategies and trades
    * Abnob Doss
    * Developed in HTML5 and JQuery with Mustache 

 
* __Core Business Logic:__
   
     Monitoring live market data
	* Vicki Lee 
	* Developed in Java using Spring and a "mock" data feed server

    Continually executing strategies based on user configurations
	* Vicki Lee | Abnob Doss
	* Developd in Java using Spring

    Ordering trades in the market
	* Julie Rosenbaum
	* Developed in Java using Spring and Java Message Service (JMS)

* __Persistence:__ 
Maintaining a database containing stock, strategy, trade, and live market data, and mapping that data to Java objects to execute strategy decisions
    * Julie Rosenbaum 
    * Developed in Java using a Spring Data Rest app with JDBC repositories and a MySQL database
