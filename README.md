# Person CRUD

API REST para gerenciamento de pessoas, desenvolvida com Java 21, Spring Boot, MongoDB, Redis, RabbitMQ, autenticação JWT, Bean Validation e documentação OpenAPI.

A aplicação permite criar, listar, filtrar, atualizar e remover pessoas, incluindo endereços e telefones vinculados. Também realiza normalização de dados, valida CPF, e-mail, datas, endereços e telefones, protege endpoints com JWT e registra logs assíncronos de requisições utilizando RabbitMQ.

## Funcionalidades

- CRUD de pessoas
- Endereços e telefones aninhados
- Paginação, filtros e ordenação
- Validação de CPF
- Normalização de dados de entrada
- Validação de e-mail e CPF únicos
- Tratamento global de exceções
- Documentação com OpenAPI/Swagger
- Autenticação por API Key
- Geração de token JWT
- Armazenamento e revogação de tokens com Redis
- Logout com invalidação de token
- Logs assíncronos de requisições com RabbitMQ
- Persistência dos logs no MongoDB
- Ambiente local com Docker Compose
- Collection Postman para testes manuais

## Stack utilizada

- Java 21
- Spring Boot 4.0.7
- Spring Web MVC
- Spring Data MongoDB
- Spring Security
- Spring Validation
- Spring Data Redis
- Spring AMQP
- JWT com Nimbus JOSE JWT
- Springdoc OpenAPI
- Lombok
- MongoDB 7
- Redis 7
- RabbitMQ 3 Management
- Docker Compose
- Maven Wrapper

> Caso o projeto esteja usando Spring Boot 3.5.x em vez de Spring Boot 4.x, ajuste esta seção conforme a versão real configurada no `pom.xml`.

## Requisitos

- Java 21+
- Docker
- Docker Compose

Não é necessário ter Maven instalado localmente, pois o projeto inclui `mvnw` e `mvnw.cmd`.

## Como executar o projeto

Suba os containers de infraestrutura:

```bash
docker compose up -d
```

Esse comando irá iniciar:

- MongoDB
- Redis
- RabbitMQ

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Serviços Docker

| Serviço | Porta | Descrição |
| --- | --- | --- |
| MongoDB | `27017` | Banco principal da aplicação |
| Redis | `6379` | Armazenamento dos tokens JWT |
| RabbitMQ | `5672` | Broker de mensagens |
| RabbitMQ Management | `15672` | Painel web do RabbitMQ |

Painel do RabbitMQ:

```text
http://localhost:15672
```

Credenciais padrão do RabbitMQ:

```text
Usuário: username
Senha: password
```

## Configuração

A configuração padrão está em:

```text
src/main/resources/application.yaml
```

Exemplo:

```yaml
spring:
  application:
    name: person-crud

  data:
    mongodb:
      uri: mongodb://username:password@localhost:27017/person_crud?authSource=admin
      auto-index-creation: true

    redis:
      host: localhost
      port: 6379

  rabbitmq:
    host: localhost
    port: 5672
    username: username
    password: password

server:
  port: 8080

springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs

app:
  security:
    api-key: person-crud-api-key-123
    jwt-secret: 1f0b4d9e2a3c5b7d8e9f0123456789abcdef123456789abcdef987654321abcd
    jwt-expiration-minutes: 60

  messaging:
    request-log-exchange: person-crud.request-log.exchange
    request-log-queue: person-crud.request-log.queue
    request-log-routing-key: person-crud.request-log.created
```

O arquivo `docker-compose.yml` cria os serviços com as mesmas credenciais usadas na configuração da aplicação.

### MongoDB

```text
Usuário: username
Senha: password
Banco: person_crud
Porta: 27017
```

### Redis

```text
Host: localhost
Porta: 6379
```

### RabbitMQ

```text
Usuário: username
Senha: password
Porta AMQP: 5672
Porta Management: 15672
```

## Documentação da API

Com a aplicação em execução, a documentação OpenAPI estará disponível em:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

O repositório também inclui uma collection Postman:

```text
person-crud-java-api-fase-4-logs.postman_collection.json
```

## Fluxo de autenticação

A maioria dos endpoints é protegida por autenticação JWT.

Fluxo utilizado:

```text
1. O cliente envia uma API Key para /api/v1/auth/validate
2. A API valida a chave
3. A API gera um JWT
4. O ID do JWT é salvo no Redis com tempo de expiração
5. O cliente envia Authorization: Bearer <token>
6. A API valida o JWT e verifica se ele ainda existe no Redis
7. O logout remove o token do Redis
```

### Validar API Key

```bash
curl -X POST "http://localhost:8080/api/v1/auth/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "apiKey": "person-crud-api-key-123"
  }'
```

Exemplo de resposta:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Use o token retornado nos endpoints protegidos:

```text
Authorization: Bearer <accessToken>
```

### Logout

```bash
curl -X POST "http://localhost:8080/api/v1/auth/logout" \
  -H "Authorization: Bearer <accessToken>"
```

Após o logout, o mesmo token é revogado no Redis e não consegue mais acessar endpoints protegidos.

## Endpoints da API

### Auth

Caminho base:

```text
/api/v1/auth
```

| Método | Caminho | Autenticação | Descrição |
| --- | --- | --- | --- |
| `POST` | `/api/v1/auth/validate` | Público | Valida a API Key e gera um JWT |
| `POST` | `/api/v1/auth/logout` | Bearer token | Revoga o token atual |

### Persons

Caminho base:

```text
/api/v1/persons
```

| Método | Caminho | Autenticação | Descrição |
| --- | --- | --- | --- |
| `GET` | `/api/v1/persons` | Bearer token | Lista pessoas com paginação, filtros e ordenação |
| `GET` | `/api/v1/persons/{id}` | Bearer token | Busca uma pessoa pelo ID |
| `POST` | `/api/v1/persons` | Bearer token | Cria uma pessoa |
| `PUT` | `/api/v1/persons/{id}` | Bearer token | Atualiza uma pessoa |
| `PATCH` | `/api/v1/persons/{id}` | Bearer token | Atualiza parcialmente uma pessoa |
| `PUT` | `/api/v1/persons/{id}/addresses` | Bearer token | Substitui os endereços de uma pessoa |
| `PUT` | `/api/v1/persons/{id}/phone-numbers` | Bearer token | Substitui os telefones de uma pessoa |
| `DELETE` | `/api/v1/persons/{id}` | Bearer token | Remove uma pessoa |

### Logs de requisição

Caminho base:

```text
/api/v1/logs
```

| Método | Caminho | Autenticação | Descrição |
| --- | --- | --- | --- |
| `GET` | `/api/v1/logs` | Bearer token | Lista logs de requisições com paginação |

## Parâmetros de listagem

### Persons

| Parâmetro | Padrão | Descrição |
| --- | --- | --- |
| `name` | nenhum | Filtra por nome ou sobrenome |
| `email` | nenhum | Filtra por e-mail |
| `documentNumber` | nenhum | Filtra por CPF |
| `page` | `0` | Número da página |
| `size` | `10` | Tamanho da página, limitado a 100 |
| `sortBy` | `createdAt` | Campo utilizado para ordenação |
| `direction` | `desc` | Direção da ordenação |

Valores permitidos para `sortBy`:

```text
firstName, lastName, email, documentNumber, dateOfBirth, createdAt, updatedAt
```

Exemplo:

```bash
curl "http://localhost:8080/api/v1/persons?page=0&size=10&sortBy=createdAt&direction=desc" \
  -H "Authorization: Bearer <accessToken>"
```

### Logs

| Parâmetro | Padrão | Descrição |
| --- | --- | --- |
| `page` | `0` | Número da página |
| `size` | `10` | Tamanho da página, limitado a 100 |

Exemplo:

```bash
curl "http://localhost:8080/api/v1/logs?page=0&size=10" \
  -H "Authorization: Bearer <accessToken>"
```

## Exemplo de criação de pessoa

```bash
curl -X POST "http://localhost:8080/api/v1/persons" \
  -H "Authorization: Bearer <accessToken>" \
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

## Exemplo de resposta

```json
{
  "id": "6870f...",
  "firstName": "Joao",
  "lastName": "Silva",
  "email": "joao.silva@example.com",
  "documentNumber": "52998224725",
  "dateOfBirth": "1990-05-20",
  "addresses": [
    {
      "street": "Rua das Flores",
      "number": "123",
      "complement": "Apto 45",
      "neighborhood": "Centro",
      "city": "Fortaleza",
      "state": "CE",
      "zipCode": "60000000"
    }
  ],
  "phoneNumbers": [
    {
      "number": "85999999999",
      "type": "Celular"
    }
  ],
  "createdAt": "2026-07-10T10:00:00",
  "updatedAt": "2026-07-10T10:00:00"
}
```

## Logs assíncronos de requisição

A aplicação possui logging assíncrono de requisições HTTP.

Fluxo:

```text
Requisição HTTP
    ↓
RequestLoggingFilter captura metadados
    ↓
RequestLogProducer publica mensagem no RabbitMQ
    ↓
RequestLogConsumer consome a mensagem
    ↓
RequestLogRepository persiste o log no MongoDB
```

Informações registradas:

- Data e hora da requisição
- Data e hora da resposta
- Duração em milissegundos
- Método HTTP
- Caminho da requisição
- Query string
- Status code
- Endereço remoto
- User-Agent
- Usuário autenticado
- Headers seguros

Headers sensíveis não são registrados, incluindo:

```text
Authorization
Cookie
Set-Cookie
```

O corpo da requisição não é registrado intencionalmente, evitando persistir dados sensíveis como CPF, e-mail, tokens ou outras informações pessoais.

Exemplo de consulta de logs:

```bash
curl "http://localhost:8080/api/v1/logs?page=0&size=10" \
  -H "Authorization: Bearer <accessToken>"
```

Exemplo de resposta:

```json
{
  "content": [
    {
      "id": "6870f...",
      "requestTime": "2026-07-10T10:00:00",
      "responseTime": "2026-07-10T10:00:00",
      "durationMs": 42,
      "method": "GET",
      "path": "/api/v1/persons",
      "queryString": "page=0&size=10",
      "statusCode": 200,
      "remoteAddress": "127.0.0.1",
      "userAgent": "PostmanRuntime/...",
      "authenticatedUser": "api-client"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

## Regras de validação

- `firstName` é obrigatório na criação e deve ter no máximo 80 caracteres.
- `lastName` é obrigatório na criação e deve ter no máximo 120 caracteres.
- `email` é obrigatório na criação e deve ser válido.
- `documentNumber` é obrigatório na criação e deve ser um CPF válido.
- `dateOfBirth` é obrigatório na criação e deve estar no passado.
- Os campos de endereço validam rua, número, bairro, cidade, estado e CEP.
- Telefones validam formatos brasileiros.
- E-mail e CPF devem ser únicos.
- CPF, CEP e telefones são normalizados antes da persistência.
- E-mail é normalizado para letras minúsculas antes da persistência.

## Exemplos de erro

Erro de validação:

```json
{
  "timestamp": "2026-07-10T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "path": "/api/v1/persons",
  "details": [
    "documentNumber: CPF inválido"
  ]
}
```

Erro de autenticação:

```json
{
  "timestamp": "2026-07-10T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Não autorizado",
  "path": "/api/v1/persons",
  "details": [
    "Full authentication is required to access this resource"
  ]
}
```

## Executando testes

```bash
./mvnw test
```

> Os testes automatizados serão implementados em uma próxima fase.

## Estrutura do projeto

```text
src/main/java/com/joaopaulofg/personcrud
├── auth
│   ├── controller
│   ├── dto
│   └── service
├── config
│   ├── AppSecurityProperties.java
│   ├── JacksonConfig.java
│   ├── MongoAuditingConfig.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── log
│   ├── config
│   ├── consumer
│   ├── controller
│   ├── dto
│   ├── filter
│   ├── model
│   ├── producer
│   ├── repository
│   └── service
├── person
│   ├── controller
│   ├── dto
│   ├── model
│   ├── repository
│   └── service
├── security
│   ├── JwtAuthenticationFilter.java
│   └── RestAuthenticationEntryPoint.java
└── shared
    ├── exception
    ├── pagination
    └── validation
```

## Comandos úteis

Subir infraestrutura:

```bash
docker compose up -d
```

Parar infraestrutura:

```bash
docker compose down
```

Parar infraestrutura e remover volumes:

```bash
docker compose down -v
```

Executar API:

```bash
./mvnw spring-boot:run
```

Executar testes:

```bash
./mvnw test
```

Gerar build:

```bash
./mvnw clean package
```

Abrir RabbitMQ Management:

```text
http://localhost:15672
```

Abrir Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Fluxo sugerido de teste

Usando Postman:

```text
1. Auth / Validate API Key
2. Persons / Create Person
3. Persons / List Persons - Paginated
4. Persons / Get Person by ID
5. Persons / Update Person - PATCH
6. Logs / List Request Logs - Paginated
7. Auth / Logout
8. Auth / Access With Current Token
```

Comportamento esperado:

- Antes da autenticação, endpoints protegidos retornam `401`.
- Após autenticação, endpoints protegidos retornam sucesso.
- Após logout, o mesmo token é revogado e endpoints protegidos retornam `401`.
- Após chamar endpoints de pessoas, os logs ficam disponíveis em `/api/v1/logs`.

## Roadmap

- [x] Fase 1: Base da API com Spring Boot e MongoDB
- [x] Fase 2: CRUD completo com validação, paginação, filtros e ordenação
- [x] Fase 3: Autenticação com API Key, JWT, Redis token store e logout
- [x] Fase 4: Logging assíncrono de requisições com RabbitMQ
- [ ] Fase 5: Testes automatizados com JUnit, Mockito, MockMvc e Testcontainers
- [ ] Fase 6: Dockerfile da aplicação
- [ ] Fase 7: Pipeline com GitHub Actions
- [ ] Fase 8: Profiles e variáveis de ambiente para produção
