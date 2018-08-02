FROM dockerreg.training.local:5000/java

WORKDIR /javet

COPY . .

CMD ["java", "-jar", "/javet/target/exchange-0.0.1.jar"]


# ENV DBHOST=mysql.training.local
# ENV DBNAME=javet_mysql
# ENV USERNAME=root
# ENV PASSWORD=c0nygre
# ENV ACTIVEMQA=tcp://localhost:61616
