# Terraformers Development Guide

## Overview
This guide provides step-by-step instructions for setting up and working with the Terraformers backend, covering both Python and Java workflows. It explains how to interact with the Supabase client, execute tests, and extend the project with new modules.

---

## Getting Started

### 1. Building Docker Containers
Before running the application, you need to build the Docker containers. Navigate to the backend directory and execute:
```sh
docker-compose build
docker-compose up -d   
```
This ensures all dependencies are installed and the latest code changes are reflected in the containerized environment.

---

## Python Workflow

### 1. Entering Interactive Mode
To interact with the Python environment within the Docker container, use:
```sh
cd Terraformers/backend
docker exec -it terraformers-python bash
```
This enters the interactive shell inside the container, similar to a virtual environment (venv).

### 2. Running Tests
To run tests for the Supabase client, execute:
```sh
python -m database.tests.test_client
```
If you encounter `ModuleNotFoundError`, ensure you are inside the correct directory:
```sh
root@d2370e5f45ae:/app# ls
python
root@d2370e5f45ae:/app# cd python
root@d2370e5f45ae:/app/python# ls
controller  database  model  requirements.txt
python -m database.tests.test_client
```
#### Sample Test Output:
```
Running simple Supabase client test...
🔄 Getting Supabase client...
✅ Successfully created Supabase client for https://vqiiqhfqdwwqblgewkws.supabase.co
🔄 Testing a simple query...
✅ Query successful! Found 1 records
📊 Sample data from first record:
  brain_id: 1
  brain_type: AI-Mk1
  max_speed: 10
  max_jump_height: 5
  dimensions: {'width': 0.8, 'height': 2.1, 'length': 1.8}
```

### 3. Installing Dependencies
If you need to add a new library, include it in `requirements.txt` and rebuild the container:
```sh
docker-compose build
```

### 4. Adding New Modules
Follow this directory structure:
```
module_name
│   ├── src
│   │   ├── __init__.py
│   │   ├── file.py
│   ├── tests
│   │   ├── file_test.py
```
To execute test files:
```sh
python -m module_name.tests.file_test
```

To exit interactive mode, type:
```sh
exit
```

---

## Java Workflow

### 1. Running Java Tests
Execute the following command to run Java tests inside the container:
```sh
cd Terraformers/backend
docker exec -it terraformers-java bash -c "cd java && mvn test"
```

### 2. Checking Test Reports
Test reports can be found in:
```
Terraformers/backend/java/target/surefire-reports
```

### 3. Adding Dependencies
To add dependencies, update the `pom.xml` file inside `<dependencies></dependencies>` and rebuild the container.

### 4. Adding New Modules
Follow this directory structure:
```
module_name
│   ├── src
│   │   ├── Class.java
│   ├── tests
│   │   ├── ClassTest.java
```
To execute test files, ensure they follow Maven's standard testing convention.

---

## Summary
- **Docker setup**: `docker-compose build`
- **Python**:
  - Enter: `docker exec -it terraformers-python bash`
  - Run tests: `python -m database.tests.test_client`
  - Add dependencies: Edit `requirements.txt` and rebuild
- **Java**:
  - Run tests: `docker exec -it terraformers-java bash -c "cd java && mvn test"`
  - Check reports: `target/surefire-reports`
  - Add dependencies: Modify `pom.xml`

By following this guide, you should be able to efficiently work with both Python and Java components in the Terraformers backend. 🚀

