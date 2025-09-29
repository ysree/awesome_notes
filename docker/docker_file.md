# Use an official base image
FROM ubuntu:22.04 AS base

# Metadata about the image
LABEL maintainer="your.email@example.com" \
      version="1.0" \
      description="Comprehensive Dockerfile example covering all keywords"

# Arguments for build-time variables
ARG APP_VERSION=1.0.0
ARG DEBIAN_FRONTEND=noninteractive

# Environment variables
ENV APP_HOME=/usr/src/app \
    PATH=$PATH:/usr/src/app/bin \
    LANG=C.UTF-8

# Set working directory
WORKDIR $APP_HOME

# Copy files from host into container
COPY src/ $APP_HOME/

# Add remote file or local tarball (supports URL)
ADD https://example.com/somefile.tar.gz /tmp/

# Install dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip curl && \
    pip3 install --no-cache-dir -r requirements.txt && \
    rm -rf /var/lib/apt/lists/*

# Create volume for persistent storage
VOLUME ["/usr/src/app/data"]

# Set default user
USER appuser

# Expose port
EXPOSE 8080

# Healthcheck for container
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# ONBUILD triggers for child images
ONBUILD COPY init-scripts/ /docker-entrypoint-init.d/

# Default command (can be overridden by docker run)
CMD ["python3", "app.py"]

# Entry point to ensure the container always runs a command
ENTRYPOINT ["sh", "-c"]
