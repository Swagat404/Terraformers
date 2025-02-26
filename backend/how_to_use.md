## Simple Workflow
1. Create/Update Schema:
   Just edit docker/schema.sql

2. Reset Database:

```bash
docker-compose down -v && docker-compose up -d
```

That's it! PostgreSQL will automatically run schema.sql when the container starts. 


```bash
docker-compose -f docker/docker-compose.yml up -d	Start services    #This command starts the backend services along with a PostgreSQL database.
docker-compose -f docker/docker-compose.yml down	Stop and remove containers
docker-compose -f docker/docker-compose.yml build	Rebuild images
```



Example Commands
Build and Start the Containers:
docker-compose -f docker/docker-compose.yml up --build -d

Stop the Containers:
docker-compose -f docker/docker-compose.yml down

Running Commands Inside the Container
To run commands inside the backend container, you can use docker exec:

Open an interactive terminal session inside the backend container:
docker exec -it docker-backend-1 bash

Install the psycopg2-binary package:
python3 -m pip install psycopg2-binary

Run the test_database.py script:
python3 test_database.py

exit 

verify container is running
docker-compose -f docker/docker-compose.yml ps





////new


docker-compose build

docker exec -it terraformers-java bash

python -m database.tests.test_client

docker exec -it terraformers-java bash -c "cd java && mvn test"