FROM dockerreg.training.local:5000/java

WORKDIR /javet

COPY . .

CMD ["java", "-jar", "/javet/target/exchange-0.0.1.jar"]


# ENV DBHOST=mysql.training.local
# ENV DBNAME=javet_mysql
# ENV USERNAME=root
# ENV PASSWORD=c0nygre
# ENV ACTIVEMQA=tcp://localhost:61616
# ENV PUBFQDNU=

# spring.datasource.url=jdbc:mysql://localhost:3306/javet
# spring.datasource.username=root
# spring.datasource.password=c0nygre
# spring.datasource.driverClassName=com.mysql.jdbc.Driver
# server.port=8082

# spring.jpa.properties.hibernate.id.new_generator_mappings=false


# ogging.level.com.citi.exchange.rest=${level}
# logging.file=myapplication.log

# spring.data.rest.basePath=/

# market.feed.url=http://feed.conygre.com:8080/MockYahoo/quotes.csv

# spring.activemq.user=admin
# spring.activemq.password=admin
# spring.activemq.broker-url=tcp://localhost:61616