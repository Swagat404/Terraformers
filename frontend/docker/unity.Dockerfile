FROM unityci/editor:2023.2.2f1-webgl-2.0 AS builder

WORKDIR /app
COPY . .

RUN unity-editor \
    -batchmode \
    -nographics \
    -projectPath /app \
    -executeMethod BuildScript.BuildWebGL \
    -logFile /app/Logs/unity_build.log \
    -quit

FROM nginx:alpine
COPY --from=builder /app/Builds/WebGL /usr/share/nginx/html







