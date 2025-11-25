# JobOffers 🚀

A **Spring Boot–based job offers aggregator** designed to fetch, store, and serve job postings for **Junior Java Developers**.  
This project demonstrates modern backend engineering practices, clean architecture, and production‑ready integrations — built to showcase both technical depth and professional craftsmanship.

---

## ✨ Features

- **REST API** with secure endpoints (`Spring Boot Web + RestControllers`)  
- **User authentication & authorization** with **JWT tokens** (`Spring Security`)  
- **Registration & login flow** with validation (`Spring Validation`)  
- **Job offers fetching** from a **remote HTTP server** (simulated with WireMock)  
- **Automatic updates** every 3 hours (`Spring Scheduler`)  
- **Caching layer** with **Redis** to reduce database load  
- **Duplicate prevention** (unique offer URLs)  
- **CRUD operations**:  
  - Fetch all offers (authorized users only)  
  - Fetch single offer by ID  
  - Add offers manually  

---

## 🛠️ Tech Stack

- **Language & Frameworks**: Java 17, Spring Boot (Web, Data MongoDB, Security, Validation, Test)  
- **Database**: MongoDB + MongoExpress (UI)  
- **Caching**: Redis + Jedis + Redis Commander  
- **Testing**:  
  - Unit tests: JUnit 5, Mockito, AssertJ  
  - Integration tests: SpringBootTest, MockMvc, SpringSecurityTest  
  - Advanced testing: WireMock (mock external APIs), Testcontainers (real DB in Docker), Awaitility (async testing)  
- **Build & Dependency Management**: Maven  
- **Logging**: Log4j2  
- **API Documentation**: Swagger / OpenAPI  
- **DevOps & Tools**: Docker, Docker Compose, Docker Desktop, GitHub/GitLab CI/CD ready  
- **IDE**: IntelliJ IDEA Ultimate  
- **Version Control**: Git  

---

## 📐 Architecture Highlights

- **Facade pattern** for clean separation of concerns (`OfferFacade`, `LoginAndRegisterFacade`)  
- **Repository pattern** with in‑memory test implementations and MongoDB persistence  
- **DTOs** for API contracts, ensuring validation and immutability  
- **Integration tests** simulate real user flows (register → login → fetch offers → caching)  
- **WireMock stubs** emulate external job offer providers  
- **Testcontainers** spin up real MongoDB/Redis instances for reliable integration testing  

---

## 🚀 Getting Started

### Prerequisites
- Java 17  
- Docker & Docker Compose  
- Maven  

### Run locally
```bash
# Build the project
mvn clean install

# Start services (MongoDB, Redis, MongoExpress, Redis Commander)
docker-compose up -d

# Run the application
mvn spring-boot:run
```

Swagger UI will be available at:  
👉 `http://localhost:8080/swagger-ui.html`

---

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Highlights:
- **Unit tests** for domain logic  
- **Integration tests** with `SpringBootTest` + `MockMvc`  
- **Security tests** with `SpringSecurityTest`  
- **Async tests** with Awaitility  
- **External API stubbing** with WireMock  
- **Real DB tests** with Testcontainers  

---

## 🎯 Why This Project Matters

This project is more than a coding exercise — it demonstrates:

- Ability to design **secure, scalable APIs**  
- Hands‑on experience with **modern Java/Spring ecosystem**  
- Proficiency in **testing strategies** (unit, integration, async, external stubs, containers)  
- Comfort with **DevOps tooling** (Docker, CI/CD, GitHub/GitLab)  
- Writing **clean, maintainable code** with Lombok, DTOs, and layered architecture  

---

## 👩‍💻 Author

Developed by **Anastazja** — backend engineer passionate about **Java, Spring, and clean architecture**.  
Looking forward to building scalable systems ✨
Email: anastazjaglowska12345@gmail.com
