# Sử dụng image Java
FROM openjdk:8-jre-slim

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép tệp JAR vào container
COPY build/libs/Final_case_social_web-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
