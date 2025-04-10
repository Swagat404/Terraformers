FROM nginx:alpine

# Copy only necessary files and directories
COPY mars-rover-simulator/ /usr/share/nginx/html/mars-rover-simulator/
COPY Images/ /usr/share/nginx/html/Images/
COPY Landingpage.html /usr/share/nginx/html/
COPY AboutUs.html /usr/share/nginx/html/
COPY SelectAvatar.html /usr/share/nginx/html/
COPY SelectYourTerrain.html /usr/share/nginx/html/

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]