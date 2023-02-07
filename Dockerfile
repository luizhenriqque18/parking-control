FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests 

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
ARG JAR_FILE=parking-control-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/${JAR_FILE} app.jar
CMD java -Dserver.port=$PORT -jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["sh","-c","java","${JAVA_OPTS}","-jar","app.jar"]
