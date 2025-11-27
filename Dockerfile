# ----- GIAI ĐOẠN 1: Build (Xây dựng) -----
# Sử dụng một image Maven (đã cài Java 17) để build file .war
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install

# ----- GIAI ĐOẠN 2: Run (Chạy) -----
# Sử dụng image Tomcat (đã cài Java 17) để chạy
FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*

# Sao chép file .war TỪ GIAI ĐOẠN 1 vào thư mục webapps của Tomcat
COPY --from=builder /app/target/C12sql-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war