FROM mcr.microsoft.com/dotnet/sdk:6.0

# Install PostgreSQL client tools
RUN apt-get update && \
    apt-get install -y postgresql-client && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the project files
COPY csharp/*.csproj /app/csharp/

# Restore NuGet packages (download dependencies)
RUN dotnet restore /app/csharp/*.csproj
