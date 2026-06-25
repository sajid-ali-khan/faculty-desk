#!/bin/bash
set -e

# Identify PostgreSQL version installed
PG_VERSION=$(ls /etc/postgresql/ | head -n 1)
if [ -z "$PG_VERSION" ]; then
    echo "Error: PostgreSQL is not installed or configuration directory /etc/postgresql/ is missing."
    exit 1
fi

DATA_DIR="/var/lib/postgresql/$PG_VERSION/main"

# Check if data directory is empty (common when mounting a new external volume for persistence)
if [ ! -d "$DATA_DIR" ] || [ -z "$(ls -A "$DATA_DIR")" ]; then
    echo "PostgreSQL data directory is empty or does not exist. Initializing a new database cluster..."
    mkdir -p "$DATA_DIR"
    chown -R postgres:postgres /var/lib/postgresql
    
    # Recreate the PostgreSQL cluster
    pg_dropcluster "$PG_VERSION" main || true
    pg_createcluster "$PG_VERSION" main
fi

# Ensure correct permissions on the data directory
chown -R postgres:postgres /var/lib/postgresql

# Start PostgreSQL service
echo "Starting PostgreSQL $PG_VERSION..."
service postgresql start

# Wait for PostgreSQL to start up and be ready
echo "Waiting for PostgreSQL to be ready on port 5432..."
until pg_isready -h localhost -p 5432; do
    sleep 1
done

# Initialize User and Database if they do not exist
echo "Checking and configuring PostgreSQL database..."

# 1. Create User 'sajid' with password 'sajid@1584' if not exists
user_exists=$(su - postgres -c "psql -tAc \"SELECT 1 FROM pg_roles WHERE rolname='sajid'\"")
if [ "$user_exists" != "1" ]; then
    echo "Creating PostgreSQL user 'sajid'..."
    su - postgres -c "psql -c \"CREATE USER sajid WITH PASSWORD 'sajid@1584' SUPERUSER;\""
else
    echo "PostgreSQL user 'sajid' already exists."
fi

# 2. Create Database 'college_app_db' owned by 'sajid' if not exists
db_exists=$(su - postgres -c "psql -tAc \"SELECT 1 FROM pg_database WHERE datname='college_app_db'\"")
if [ "$db_exists" != "1" ]; then
    echo "Creating PostgreSQL database 'college_app_db'..."
    su - postgres -c "psql -c \"CREATE DATABASE college_app_db OWNER sajid;\""
else
    echo "PostgreSQL database 'college_app_db' already exists."
fi

# Start the Spring Boot Application
echo "Starting the Spring Boot application..."
exec java -jar /app/college_app.jar