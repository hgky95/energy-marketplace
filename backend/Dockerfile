FROM amazoncorretto:21.0.5

WORKDIR /app

COPY . .

RUN ./gradlew build --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/*.jar"]