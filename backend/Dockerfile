# ==========================================
# 🐳 STAGE 1: BUILD STAGE
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy Maven dependency descriptor first for efficient layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# 🐳 STAGE 2: RUNTIME STAGE
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Set timezone to Vietnam (Asia/Ho_Chi_Minh)
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime && \
    echo "Asia/Ho_Chi_Minh" > /etc/timezone

# Create directory for image uploads
RUN mkdir -p /app/uploads/images

# Security: Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring && \
    chown -R spring:spring /app

USER spring:spring

# Copy built artifact from builder stage
COPY --from=builder --chown=spring:spring /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "app.jar"]
