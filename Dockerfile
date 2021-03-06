FROM java:8-jdk-alpine
COPY ./kdtrace-webapp/target/kdtrace-webapp-0.0.1-SNAPSHOT.jar /usr/app/
COPY ./blockchain/connection-kdtrace.yaml /usr/app/blockchain/
ADD ./blockchain/crypto-config /usr/app/blockchain/crypto-config
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kdtrace-webapp-0.0.1-SNAPSHOT.jar"]
