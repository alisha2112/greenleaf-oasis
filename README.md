# GreenLeaf Oasis API

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green?style=for-the-badge&logo=spring-boot)
![Postgres](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-24-blue?style=for-the-badge&logo=docker)

Надійний RESTful API для інтернет-магазину рослин. Проєкт розроблено з використанням сучасних практик Java, з акцентом на масштабованість, чисту архітектуру та ретельне тестування.

---

## Технологічний стек

* **Core:** Java 17, Spring Boot 3.4.1
* **Database:** PostgreSQL 16
* **Migration Tool:** Liquibase
* **Security:** Spring Security, JWT (JSON Web Tokens)
* **Testing:** JUnit 5, Mockito, Testcontainers (Integration Tests)
* **Documentation:** Swagger / OpenAPI 3
* **Containerization:** Docker, Docker Compose

---

## Архітектура

Проєкт побудовано за принципами **багатошарової архітектури (Layered Architecture)**, що забезпечує розділення відповідальності та легкість у підтримці:

1.  **Controller Layer** обробляє вхідні HTTP-запити та повертає відповіді, використовує DTO для відділення внутрішньої моделі даних від зовнішнього API
2.  **Service Layer** містить бізнес-логіку застосунку; тут відбувається керування транзакціями (`@Transactional`)
3.  **Repository Layer** абстракція для доступу до даних за допомогою Spring Data JPA
4.  **Database Layer** реляційна база даних PostgreSQL, структура якої керується міграціями Liquibase

---

## Структура бази даних (ER Diagram)

![ER Diagram](images/greenleaf-oasis-er-diagram.png)

---

## Як запустити проєкт

**Вимоги:**
* Docker & Docker Desktop
* Java 17
* Maven

1. Клонування репозиторію  
```
git clone https://github.com/your-username/greenleaf-oasis.git
cd greenleaf-oasis
```
2. Запуск бази даних  
Проєкт використовує Docker Compose для підняття контейнера з PostgreSQL.
```
docker-compose up -d
```
3. Запуск застосунку  
Ви можете запустити проєкт за допомогою Maven wrapper:
```
./mvnw spring-boot:run
```

---

## API Документація

Після запуску застосунку ви можете ознайомитись з документацією та протестувати ендпоінти через Swagger UI: http://localhost:8080/swagger-ui/index.html

**Тестові акаунти**

Для тестування авторизації використовуйте наступні дані:

* Admin: admin@greenleaf.com / password
* User: user@greenleaf.com / password

---

## Тестування

У проєкті реалізовані інтеграційні тести з використанням бібліотеки Testcontainers. Це гарантує, що тести виконуються в реальному оточенні PostgreSQL (у Docker-контейнері), забезпечуючи високу надійність перевірок.

Запуск усіх тестів:
```
./mvnw test
```


