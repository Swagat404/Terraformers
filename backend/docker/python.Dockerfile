FROM python:3.11-slim

# Install PostgreSQL client tools
RUN apt-get update && \
    apt-get install -y postgresql-client && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy requirements.txt
COPY python/requirements.txt /app/python/

# Install Python dependencies
RUN pip install --no-cache-dir -r /app/python/requirements.txt
