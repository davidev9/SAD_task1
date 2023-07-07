FROM maven AS build
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests


FROM eclipse-temurin:20-jdk-alpine
COPY --from=build /app/target/*.jar manvsclass.jar
ENV PORT 8080
EXPOSE 8080
CMD ["java","-jar","manvsclass.jar"]