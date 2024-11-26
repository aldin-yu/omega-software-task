FROM eclipse-temurin:17-jdk-focal AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -D skipTests

FROM eclipse-temurin:17-jre-focal AS runtime

WORKDIR /app

COPY --from=build /app/target/omega-software-task-0.0.1-SNAPSHOT.jar /app/omega-software.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/omega-software.jar"]
