FROM gradle:8.3-jdk17-jammy AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon -x test

FROM eclipse-temurin:17.0.10_7-jre

ARG PROFILE
ENV PROFILE=${PROFILE}

RUN if [ "$PROFILE" != "dev" ] && [ "$PROFILE" != "prd" ]; then echo "Invalid PROFILE argument; must be 'dev' or 'prd'."; exit 1; fi

WORKDIR /app

COPY --from=build /app/build/libs/be-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "app.jar"]