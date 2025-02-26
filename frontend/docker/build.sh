#!/bin/bash
set -e

echo "Starting Unity WebGL build for Mars AI Simulator..."

# Start Xvfb
Xvfb :99 -ac -screen 0 1280x1024x24 > /dev/null 2>&1 &
sleep 3

# Unity executable path for the new image
UNITY_EXECUTABLE="/opt/unity/Editor/Unity"

echo "Building WebGL project with $UNITY_EXECUTABLE..."
"$UNITY_EXECUTABLE" \
  -batchmode \
  -nographics \
  -projectPath /app/unity \
  -executeMethod BuildScript.BuildWebGL \
  -logFile /app/unity/Logs/unity_build.log \
  -quit

# Check build result
if [ $? -eq 0 ]; then
  echo "Unity build completed successfully!"
  echo "Build output is in /app/unity/Builds/WebGL"
  exit 0
else
  echo "Unity build failed. Check the log for details."
  cat /app/unity/Logs/unity_build.log
  exit 1
fi

