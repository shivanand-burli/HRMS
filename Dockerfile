# Stage 1: Build native image
FROM ghcr.io/graalvm/native-image-community:25 AS builder
WORKDIR /app

# Copy project
COPY . /app

# Build native binary
RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true

# Stage 2: Minimal runtime
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7
WORKDIR /app

# Copy native binary from builder
COPY --from=builder /app/target/*-runner /app/application
RUN chmod +x /app/application

# Railway port
ENV PORT=8080
EXPOSE 8080

# Run native binary, binding to Railway's dynamic port
CMD ["sh", "-c", "./application -Dquarkus.http.port=$PORT"]
