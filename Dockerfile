FROM dockerreg.training.local:5000/java

WORKDIR /javet

COPY . .

CMD ["/opt/itrs/netprobe/netprobe.linux_64", "$APPNAME", "-port", "7036", "-nopassword", "&"]

ENTRYPOINT ["java", "-jar", "/javet/exchange-0.0.1.jar"]

EXPOSE 8085

ENTRYPOINT java ${JAVAOPT} -jar target/exchange-0.0.1.jar

