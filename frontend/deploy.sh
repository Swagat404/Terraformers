#!/bin/bash

echo "Building Mars Rover Simulator Docker Image..."
docker build -t mars-rover-simulator .

echo "Testing the build locally..."
docker run -p 8000:80 --rm mars-rover-simulator

echo "To deploy to Render.com:"
echo "1. Push this repository to GitHub"
echo "2. Create a new Web Service on Render.com"
echo "3. Select 'Docker' as the environment"
echo "4. Connect your GitHub repository"
echo "5. Deploy the service"

# If you want to tag and push to a registry, uncomment these lines
# echo "Tagging and pushing to your registry..."
# docker tag mars-rover-simulator your-registry/mars-rover-simulator:latest
# docker push your-registry/mars-rover-simulator:latest