FROM dockerreg.training.local:5000/java

WORKDIR /javet

COPY . .

CMD ["/opt/itrs/netprobe/netprobe.linux_64", "$APPNAME", "-port", "7036", "-nopassword", "&"]

ENTRYPOINT ["java", "-jar", "/javet/target/exchange-0.0.1.jar"]

