# Terraformers - Mars AI Simulator

![Mars AI Simulator Banner](./assets/images/logo.png)

## Table of Contents
- [Overview](#overview)
- [File Structure](#file-structure)
- [Branching Strategy](#branching-strategy)
- [Development Environment Setup](#development-environment-setup)
- [Prerequisites](#prerequisites)
- [Docker Setup](#docker-setup)
- [Customization & Extensibility](#customization--extensibility)
- [Team Members](#team-members)

## Overview
This project is a Mars exploration simulation where users can control avatars, explore terrain, and interact with AI-driven elements. It utilizes real Mars terrain data and allows for the instantiation of robot avatars with AI or procedural brains.

## File Structure
The repository is organized as follows:

```
mars-ai-simulator/
├── README.md                                         # Project overview and setup instructions
├── assets/                                           # Extra resources like images, videos, shapes, etc.
├── backend/                                          # All backend code (APIs, AI, database logic)
│   ├── csharp/                                       # C# backend logic
│   ├── java/                                         # Java backend components
│   │   ├── com/terraformers/                         # Java package
│   │   │   ├── module_name/
│   │   │   │   ├── src/
│   │   │   │   │   └── ClassName.java
│   │   │   │   └── tests/
│   │   │   │       └── ClassNameTest.java
│   │   │   └── ...
│   │   ├── pom.xml                                  # Java config file
│   │   └── target/                                  # Maven build files
│   ├── python/                                      # Python-based backend services
│   │   ├── module_name/
│   │   │   ├── __init__.py
│   │   │   ├── module.py
│   │   │   └── tests/
│   │   │       └── test_module.py
│   │   ├── database/                               # Supabase database client module
│   │   └── ...
│   ├── docker/    # Docker container setup
│   │   ├── csharp.Dockerfile
│   │   ├── java.Dockerfile
│   │   └── python.Dockerfile
│   └── docker-compose.yml                          # Multi-container setup for backend and database
├── frontend/                                       # UI code (web interface or Unity-based UI)
│   ├── unity/
│   └── web/
├── docs/                                           # Project documentation and design specs
│   └── backend_docker_guide.md
├── data/                                           # Mars terrain data, avatar models, and related assets
│   ├── avatars/
│   ├── missions/
│   └── terrain/
├── scripts/                                        # Utility scripts (build, deployment, etc.)
├── logs/                                           # Logs and runtime outputs
```

## Branching Strategy
We follow a feature-branch workflow to maintain a clean and stable `main` branch. Here's how you can contribute new features:

### Create a Feature Branch
1. Always branch off from the latest `main` branch.
2. Use a descriptive branch name, e.g., `feature/add-new-ai-module` or `feature/ui-enhancements`.

```bash
git checkout main
git pull origin main
git checkout -b feature/your-feature-name
```

### Develop & Test
- Commit changes frequently with clear commit messages.
- Run your tests locally to ensure nothing breaks.

### Merge & Pull Request
1. When your feature is complete, open a pull request against `main`.
2. Include a description of what your branch does and any additional context.
3. After review, your branch will be merged into `main`.

### Keep Your Branch Updated
Regularly merge the latest `main` into your feature branch to avoid conflicts:

```bash
git fetch origin
git merge origin/main
```

### Clean Up (Optional)
Delete your local feature branch:

```bash
git branch -d feature/your-feature-name
```

## Development Environment Setup
### Prerequisites
Ensure you have the following installed:
- Git
- Docker

### Docker Setup
Refer to [backend_docker_guide](docs/backend_docker_guide.md) and [frontend_docker_guide](docs/frontend_docker_guide.md) for detailed Docker-related steps.


## Customization & Extensibility
If your project requires additional libraries, languages, or tools, you can customize the environment as follows:

### Adding New Tools
- If you integrate a new language or framework, consider adding a dedicated folder (e.g., `frontend/jmonkey/`) and update the Docker configuration accordingly.
- Maintain documentation in the `docs/` folder.

## Team Members
- **Team Member 1** - 
- **Team Member 2** - 
- **Team Member 3** - 

