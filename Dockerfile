FROM eclipse-temurin:21-jre-alpine

# Add metadata labels
LABEL maintainer="DevOps Student"
LABEL application="Student Management System"
LABEL version="1.0.0"

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the JAR from the Jenkins workspace (target directory)
COPY target/student-management-1.0.0.jar app.jar

# Set ownership
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/students/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
