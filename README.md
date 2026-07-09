# Person CRUD

REST API for managing people, built with Java 21, Spring Boot, MongoDB, Bean Validation, and OpenAPI documentation.

The application supports creating, listing, filtering, updating, and deleting people, including nested addresses and phone numbers. It also normalizes input values and validates CPF, email, dates, addresses, and phone numbers.

## Tech Stack

- Java 21
- Spring Boot 4.0.7
- Spring Web MVC
- Spring Data MongoDB
- Spring Validation
- Springdoc OpenAPI
- Lombok
- MongoDB 7
- Docker Compose
- Maven Wrapper

## Requirements

- Java 21+
- Docker and Docker Compose

You do not need a local Maven installation because the project includes `mvnw` and `mvnw.cmd`.

## Getting Started

Start MongoDB:

```bash
docker compose up -d
```

Run the application:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Configuration

The default configuration is in `src/main/resources/application.yaml`.

```yaml
spring:
  mongodb:
    uri: mongodb://username:password@localhost:27017/person_crud?authSource=admin

server:
  port: 8080
```

The Docker Compose file creates a MongoDB container with the same credentials:

- Username: `username`
- Password: `password`
- Database: `person_crud`
- Port: `27017`

## API Documentation

When the application is running, the OpenAPI documentation is available at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

The repository also includes a Postman collection:

```text
person-crud-java-api.postman_collection.json
```

## API Endpoints

Base path:

```text
/api/v1/persons
```

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/v1/persons` | List people with pagination, filtering, and sorting |
| `GET` | `/api/v1/persons/{id}` | Find a person by ID |
| `POST` | `/api/v1/persons` | Create a person |
| `PUT` | `/api/v1/persons/{id}` | Update a person |
| `PATCH` | `/api/v1/persons/{id}` | Partially update a person |
| `PUT` | `/api/v1/persons/{id}/addresses` | Replace a person's addresses |
| `PUT` | `/api/v1/persons/{id}/phone-numbers` | Replace a person's phone numbers |
| `DELETE` | `/api/v1/persons/{id}` | Delete a person |

### List Query Parameters

| Parameter | Default | Description |
| --- | --- | --- |
| `name` | none | Filters by name |
| `email` | none | Filters by email |
| `documentNumber` | none | Filters by CPF |
| `page` | `0` | Page number |
| `size` | `10` | Page size, limited to 100 |
| `sortBy` | `createdAt` | Sort field |
| `direction` | `desc` | Sort direction |

Allowed `sortBy` values:

```text
firstName, lastName, email, documentNumber, dateOfBirth, createdAt, updatedAt
```

Example:

```bash
curl "http://localhost:8080/api/v1/persons?page=0&size=10&sortBy=createdAt&direction=desc"
```

## Request Example

Create a person:

```bash
curl -X POST "http://localhost:8080/api/v1/persons" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Joao",
    "lastName": "Silva",
    "email": "joao.silva@example.com",
    "documentNumber": "529.982.247-25",
    "dateOfBirth": "1990-05-20",
    "addresses": [
      {
        "street": "Rua das Flores",
        "number": "123",
        "complement": "Apto 45",
        "neighborhood": "Centro",
        "city": "Fortaleza",
        "state": "CE",
        "zipCode": "60000-000"
      }
    ],
    "phoneNumbers": [
      {
        "number": "(85) 99999-9999",
        "type": "Celular"
      }
    ]
  }'
```

## Validation Rules

- `firstName` is required on create and must have at most 80 characters.
- `lastName` is required on create and must have at most 120 characters.
- `email` is required on create and must be valid.
- `documentNumber` is required on create and must be a valid CPF.
- `dateOfBirth` is required on create and must be in the past.
- Address fields validate street, number, neighborhood, city, state, and ZIP code.
- Phone numbers validate Brazilian phone number formats.
- Email and CPF must be unique.

## Running Tests

```bash
./mvnw test
```

## Project Structure

```text
src/main/java/com/joaopaulofg/personcrud
├── config
│   ├── MongoAuditingConfig.java
│   └── OpenApiConfig.java
├── person
│   ├── controller
│   ├── dto
│   ├── model
│   ├── repository
│   └── service
└── shared
    ├── exception
    ├── pagination
    └── validation
```

## Useful Commands

Start MongoDB:

```bash
docker compose up -d
```

Stop MongoDB:

```bash
docker compose down
```

Run the API:

```bash
./mvnw spring-boot:run
```

Run tests:

```bash
./mvnw test
```
