# Stage 1
FROM maven:3.8.3-openjdk-17 AS build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2
FROM openjdk:17-ea-23-jdk as extract
COPY --from=build /target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 3
FROM openjdk:17-ea-23-jdk
COPY --from=extract dependencies/ ./
COPY --from=extract snapshot-dependencies/ ./
COPY --from=extract spring-boot-loader/ ./
COPY --from=extract application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
