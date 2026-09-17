# Student Management System 🎓

A simple **Student Management REST API** built with Java and Spring Boot, demonstrating an end-to-end CI/CD pipeline using **Git**, **Maven**, **Docker**, and **Jenkins**.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 17 + Spring Boot 3.2 | Application framework |
| H2 (in-memory) | Database |
| Maven 3.9 | Build & dependency management |
| Docker | Containerization |
| Jenkins | CI/CD automation |
| Git | Version control |

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/students` | Add a new student |
| `GET` | `/students` | Get all students |
| `GET` | `/students/{id}` | Get student by ID |
| `DELETE` | `/students/{id}` | Delete student by ID |
| `GET` | `/students/health` | Health check |

---

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+

```bash
# Clone the repository
git clone <your-repo-url>
cd student-management

# Run tests
mvn clean test

# Run the application
mvn spring-boot:run
```

The app will start at **http://localhost:8080**

### Test the API

```bash
# Add a student
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Johnson","rollNumber":"CS2021001","grade":9.2,"department":"Computer Science"}'

# View all students
curl http://localhost:8080/students

# View student by ID
curl http://localhost:8080/students/1

# Health check
curl http://localhost:8080/students/health
```

### H2 Console
Access the in-memory database console at: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:studentdb`
- Username: `sa`
- Password: *(leave blank)*

---

## Docker

### Build and Run

```bash
# Build Docker image (multi-stage)
docker build -t student-management:latest .

# Run the container
docker run -d -p 8080:8080 --name student-management-app student-management:latest

# Check container logs
docker logs student-management-app

# Stop the container
docker stop student-management-app
```

---

## Maven Build

```bash
mvn clean test          # Compile + run unit tests
mvn clean package       # Build executable JAR → target/student-management-1.0.0.jar
mvn spring-boot:run     # Run the application directly
```

---

## Jenkins CI/CD Pipeline

The `Jenkinsfile` defines a declarative pipeline with the following stages:

1. **Checkout** — Pull source code from Git
2. **Build & Test** — `mvn clean test` (JUnit results published)
3. **Package** — `mvn package` (JAR archived)
4. **Build Docker Image** — `docker build`
5. **Deploy Container** — Stop old container, start new one
6. **Verify Deployment** — Health check via `curl`

### Setup Jenkins

```bash
# Run Jenkins via Docker
docker run -d \
  -p 9090:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --name jenkins \
  jenkins/jenkins:lts-jdk17
```

Then:
1. Open **http://localhost:9090**
2. Create a **Pipeline** job
3. Set Pipeline definition to **"Pipeline script from SCM"**
4. Point to your Git repository
5. Jenkins will automatically find the `Jenkinsfile`

---

## Troubleshooting & Maintenance

During this exercise, we encountered and resolved several CI/CD integration issues. Here are the commands used to fix them:

### 1. Fix Jenkins Git Workspace Corruption
If a previous build crashes and corrupts the Jenkins workspace (`error: object file is empty`), manually clear the workspace and caches from the Jenkins container:
```bash
docker exec jenkins sh -c "rm -rf /var/jenkins_home/workspace/student-management*"
docker exec jenkins sh -c "rm -rf /var/jenkins_home/caches/git-*"
```

### 2. Fix Docker Socket Permission Denied in Jenkins
If the Jenkins pipeline fails at the Docker Build stage with `permission denied while trying to connect to the Docker daemon socket`:
```bash
docker exec -u root jenkins chmod 666 /var/run/docker.sock
```

### 3. Fix Docker Desktop Memory Crashes
If Docker crashes or stops automatically during resource-intensive Maven/Java builds, limit WSL's memory usage by creating a `.wslconfig` file in your Windows User folder (`C:\Users\<username>\.wslconfig`):
```ini
[wsl2]
memory=8GB 
processors=4
swap=4GB
```
Then restart WSL and Docker:
```powershell
wsl --shutdown
# Then manually reopen Docker Desktop
```

### 4. Clear Old Docker Builds
To clean up space from previous crashed or dangling Docker images:
```bash
docker system prune -a -f
```

---

## Project Structure

```
student-management/
├── src/
│   ├── main/java/com/devops/student/
│   │   ├── StudentManagementApplication.java  ← Main entry point
│   │   ├── controller/StudentController.java  ← REST API
│   │   ├── service/StudentService.java        ← Business logic
│   │   ├── repository/StudentRepository.java  ← Data access
│   │   └── model/Student.java                 ← Entity
│   ├── main/resources/
│   │   └── application.properties             ← Configuration
│   └── test/java/com/devops/student/
│       └── StudentServiceTest.java            ← Unit tests
├── pom.xml                                    ← Maven build config
├── Dockerfile                                 ← Multi-stage Docker build
├── Jenkinsfile                                ← CI/CD pipeline
├── .gitignore
└── README.md
```

---

## Student Data Format

```json
{
  "name": "Alice Johnson",
  "rollNumber": "CS2021001",
  "grade": 9.2,
  "department": "Computer Science"
}
```

---

*DevOps Lab Assignment — SNU SEM 7*
