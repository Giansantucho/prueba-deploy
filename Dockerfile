FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/tp_dds_g1_2024-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY ${JAR_FILE} tp_dds_g1_2024.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","tp_dds_g1_2024.jar"]