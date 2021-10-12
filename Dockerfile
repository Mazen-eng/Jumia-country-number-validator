
From openjdk:8-jdk-alpine
COPY ./target/jumia-validator-app.jar jumia-validator-app.jar
COPY ./target/classes/sample.db sample.db
ENTRYPOINT ["java","-jar","jumia-validator-app.jar"]