FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드된 jar 이름에 맞게 수정
COPY build/libs/ecommerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
