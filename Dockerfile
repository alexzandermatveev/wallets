FROM openjdk:17-jdk-slim as builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/testCase1-0.0.1-SNAPSHOT.jar /app/testCase1.jar

EXPOSE 8081

CMD ["java", "-jar", "testCase1.jar"]
