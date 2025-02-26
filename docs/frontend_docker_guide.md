# Navigate to the project directory
cd frontend

# Build and run the Docker container
docker-compose build unity-builder
docker-compose up unity-builder


# For interactive access to the container
docker-compose run --rm unity-dev


# Stop any running containers first
docker-compose down

# Rebuild the image with your changes
docker-compose build unity-builder

# Run the container again
docker-compose up unity-builder


5. Expected Result
After building with Docker, you'll have a WebGL build in frontend/unity/Builds/WebGL/ that you can run in a browser. When opened, it will immediately show:

A Mars-themed UI canvas
The title "Mars AI Simulator"
A connect button
Status information
This setup gives you a fully functional starting point for your Mars AI Simulator frontend, with Docker integration for consistent builds across platforms.