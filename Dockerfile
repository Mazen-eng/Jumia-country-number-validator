
From adoptopenjdk/openjdk11:ubi
COPY ./target/jumia-validator-app.jar jumia-validator-app.jar
COPY ./target/classes/sample.db sample.db
ENTRYPOINT ["java","-jar","jumia-validator-app.jar"]