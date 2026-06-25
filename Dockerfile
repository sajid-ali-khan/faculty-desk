# Step 1: Use a base image that includes both Java and basic Linux utils (like service/pg_isready)
# Note: Since your script uses 'apt' style paths (/etc/postgresql), an Ubuntu/Debian based JDK image is best.
FROM eclipse-temurin:17-jdk

# Step 2: Install PostgreSQL inside the image so the script can configure it
RUN apt-get update && apt-get install -y postgresql postgresql-contrib && rm -rf /var/lib/apt/lists/*

# Step 3: Set working directory
WORKDIR /app

# Step 4: Copy the compiled Spring Boot jar from the target folder
COPY target/*.jar /app/college_app.jar

# Step 5: Copy the entrypoint script into the container
COPY entrypoint.sh /app/entrypoint.sh

# Step 6: Grant execution privileges to the script
RUN chmod +x /app/entrypoint.sh

# Step 7: Define the script as the container entrypoint
ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh"]