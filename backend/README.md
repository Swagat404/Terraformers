# Permanent Storage Component 

I had already started with a local Docker Compose setup using PostgreSQL and built a Python module to manage our database operations
creating tables for missions, logs, and results, plus functions to insert data and export logs to CSV. I'm open to testing out 
the Supabase project as an alternative if that suits the team's needs better.

## What I've Done

- **Docker Compose Setup:**  
  Configured our `docker-compose.yml` in the `backend` folder to run services for Java, Python, C#, and a PostgreSQL database (named `mars_db`).
  The PostgreSQL service uses a named volume for persistent storage and communicates with other containers through our Docker network.

- **Environment Configuration:**  
  Created a `.env` file (in `backend`) with the following settings:
  ```env
  DB_HOST=db
  DB_PORT=5432
  DB_NAME=mars_db
  DB_USER=mars_user
  DB_PASSWORD=supersecurepassword
This ensures all services connect reliably to the database.

## Database Schema:
The database/schema.sql file sets up tables for:

missions: Mission ID, start/end times, status, and objectives.
logs: Recording log events.
results: Mission outcomes and details.
Python DB Manager:
The python/db_manager.py module includes functions to insert mission data, log events, and export logs to CSV for reporting.

## How to Use
1.Start the Environment:
   In the backend directory, run:
   
         docker compose up -d

2.Apply the Schema:
    From the backend folder, run:
    
         docker exec -i mars_db psql -U mars_user -d mars_db < database/schema.sql
         
3.Using the Python Module:
    Enter the Python container:
    
         docker exec -it terraformers-python bash
         
    Navigate to /app/python and start Python
    
         cd /app/python
         python3
         
    In the Python interpreter, run:
    
         from db_manager import insert_mission, insert_log, export_logs_to_csv

         mission_id = insert_mission("Test Mission", "Collect samples on Mars")
         
         insert_log(mission_id, "Mission started successfully.")
         
         insert_log(mission_id, "Avatar initiated movement.")
         
         export_logs_to_csv(mission_id, "test_mission_logs.csv")
         
         Exit the interpreter (exit() or Ctrl+D) and then exit the container shell.

— Arok
