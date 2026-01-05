# Customer Management API

API para gerenciamento de clientes e seus endereços.

## 📋 Visão Geral

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot para gerenciar informações de clientes. A API permite criar, consultar, atualizar clientes e seus endereços.

## 🚀 Tecnologias Utilizadas

*   **Java 21**: Linguagem de programação.
*   **Spring Boot 3.5.9**: Framework para desenvolvimento da aplicação.
*   **PostgreSQL**: Banco de dados relacional.
*   **Flyway**: Ferramenta de migração de banco de dados.
*   **OpenAPI (Swagger)**: Especificação e documentação da API.
*   **Gradle**: Ferramenta de automação de build.
*   **MapStruct**: Mapeamento de objetos (DTO <-> Entity).
*   **Lombok**: Redução de código boilerplate.
*   **JUnit 5 & Mockito**: Testes unitários.

## 🛠️ Configuração e Execução

### Pré-requisitos

*   Java 21 instalado.
*   Gradle (ou use o wrapper `gradlew` incluído).

### Build e Execução

Para compilar o projeto e gerar os artefatos (incluindo código gerado pelo OpenAPI):

```bash
./gradlew clean build
```

Para executar a aplicação localmente (assumindo que o banco de dados já esteja configurado):

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

> **Nota:** Existe um projeto auxiliar responsável por orquestrar a infraestrutura local (banco de dados, etc.) via Docker Compose. Consulte a documentação desse projeto para subir o ambiente completo.

## 🏗️ Estrutura do Projeto

O projeto segue uma arquitetura em camadas (Clean Architecture/Hexagonal simplificada):

*   `api`: Contratos e DTOs gerados pelo OpenAPI.
*   `application`: Casos de uso (regras de negócio) e exceções.
*   `infrastructure`:
    *   `adapters/inbound`: Controladores REST.
    *   `adapters/outbound`: Entidades JPA e Repositórios.
    *   `mappers`: Conversores de objetos.
    *   `configs`: Configurações do Spring.

## 📝 Notas Técnicas

*   **Virtual Threads**: O projeto está configurado para utilizar Virtual Threads (Java 21+), proporcionando alta escalabilidade para operações de I/O.
*   **Performance de Banco de Dados**: O Hibernate está configurado para realizar *batch inserts* e *updates*, otimizando a performance em operações de escrita em lote.
*   **Distributed Tracing**: A aplicação suporta rastreamento distribuído, propagando e mantendo o `traceId` recebido de clientes (ex: `insurance-service`) para facilitar a observabilidade e debug em arquiteturas de microsserviços.
*   **Rate Limiter**: Controle de vazão da API, limitando em 50 RPS para operações de leitura (com fail-fast) e 10 RPS para operações de escrita com janela de espera de 2s, retornando HTTP 429.

## 🗄️ Banco de Dados

A estrutura do banco de dados é gerenciada pelo Flyway.

*   **Tabelas**:
    *   `CUSTOMER`: Armazena dados pessoais do cliente (Nome, CPF, Email, etc.).
    *   `ADDRESS`: Armazena endereços vinculados aos clientes.

As migrações estão localizadas em `src/main/resources/db/migration`.

## 🧪 Testes

Para executar os testes unitários:

```bash
./gradlew test
```

## 📚 Documentação da API

A documentação da API é gerada automaticamente via OpenAPI.

*   **Swagger UI**: Acesse `http://localhost:8080/swagger-ui/index.html` (quando a aplicação estiver rodando) para visualizar e testar os endpoints.
*   **Especificação YAML**: O contrato da API está definido em `src/main/resources/spec/customer_management_api-v1.yaml`.

### Endpoints Principais

#### Clientes (`/customers`)

*   `POST /customers`: Cria um novo cliente.
*   `GET /customers`: Lista clientes de forma paginada.
*   `GET /customers/{customerId}`: Busca um cliente pelo ID.
*   `PATCH /customers/{customerId}`: Atualiza parcialmente um cliente pelo ID.

#### Endereços (`/addresses`)

*   `PATCH /addresses/{addressId}`: Atualiza parcialmente um endereço pelo ID.

## 🔄 Fluxo de Negócio (utilizando a API)

### 1. Criação de Cliente

O endpoint recebe os dados do cliente e uma lista de endereços para cadastro.

*   **Exemplo de Chamada (cURL)**:

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/customers' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Edson Cândido",
  "email": "edd.candido@gmail.com",
  "phone": "11984173650",
  "birthDate": "1975-03-09",
  "cpf": "12345678901",
  "addresses": [
    {
      "street": "Estrada do Campo Limpo",
      "number": 5733,
      "complement": "Apto 42B Iguaçu",
      "neighborhood": "Campo Limpo",
      "city": "São Paulo",
      "state": "SP",
      "zipcode": "05787000",
      "country": "Brasil",
      "type": "RESIDENCIAL"
    },
    {
      "street": "Av Copacabana",
      "number": 5000,
      "complement": "Bloco C, Apt 101",
      "neighborhood": "Copacabana",
      "city": "Rio de Janeiro",
      "state": "RJ",
      "zipcode": "22020001",
      "country": "Brasil",
      "type": "COMERCIAL"
    }
  ]
}'
```

*   **Exemplo de Retorno**:

```json
{
  "id": 1,
  "name": "Edson Cândido",
  "birthDate": "1975-03-09",
  "cpf": "12345678901",
  "email": "edd.candido@gmail.com",
  "phone": "11984173650",
  "createdAt": "2026-01-04T12:18:54-03:00",
  "updatedAt": "2026-01-04T12:18:54-03:00",
  "addresses": [
    {
      "id": 1,
      "street": "Estrada do Campo Limpo",
      "number": 5733,
      "complement": "Apto 42B Iguaçu",
      "zipcode": "05787000",
      "neighborhood": "Campo Limpo",
      "city": "São Paulo",
      "state": "SP",
      "country": "Brasil",
      "type": "RESIDENCIAL",
      "createdAt": "2026-01-04T12:18:54-03:00",
      "updatedAt": "2026-01-04T12:18:54-03:00"
    },
    {
      "id": 2,
      "street": "Av Copacabana",
      "number": 5000,
      "complement": "Bloco C, Apt 101",
      "zipcode": "22020001",
      "neighborhood": "Copacabana",
      "city": "Rio de Janeiro",
      "state": "RJ",
      "country": "Brasil",
      "type": "COMERCIAL",
      "createdAt": "2026-01-04T12:18:54-03:00",
      "updatedAt": "2026-01-04T12:18:54-03:00"
    }
  ]
}
```

### 2. Listagem Paginada de Clientes

O endpoint recebe os parâmetros de consulta `page`, `size` e `sort` (caso não fornecidos, os valores padrão são `0`, `10` e `name,asc`) e retorna uma lista paginada.

*   **Exemplo de Chamada (cURL)**:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/customers?page=0&size=10&sort=name,asc' \
  -H 'accept: application/json'
```

*   **Exemplo de Retorno**:

```json
{
  "page": [
    {
      "id": 1,
      "name": "Edson Cândido",
      "birthDate": "1975-03-09",
      "cpf": "12345678901",
      "email": "edd.candido@gmail.com",
      "phone": "11984173650",
      "createdAt": "2026-01-04T12:18:54-03:00",
      "updatedAt": "2026-01-04T12:18:54-03:00",
      "addresses": [
        {
          "id": 1,
          "street": "Estrada do Campo Limpo",
          "number": 5733,
          "complement": "Apto 42B Iguaçu",
          "zipcode": "05787000",
          "neighborhood": "Campo Limpo",
          "city": "São Paulo",
          "state": "SP",
          "country": "Brasil",
          "type": "RESIDENCIAL",
          "createdAt": "2026-01-04T12:18:54-03:00",
          "updatedAt": "2026-01-04T12:18:54-03:00"
        },
        {
          "id": 2,
          "street": "Av Copacabana",
          "number": 5000,
          "complement": "Bloco C, Apt 101",
          "zipcode": "22020001",
          "neighborhood": "Copacabana",
          "city": "Rio de Janeiro",
          "state": "RJ",
          "country": "Brasil",
          "type": "COMERCIAL",
          "createdAt": "2026-01-04T12:18:54-03:00",
          "updatedAt": "2026-01-04T12:18:54-03:00"
        }
      ]
    }
  ],
  "pageNumber": 0,
  "pageItems": 1,
  "totalPages": 1,
  "totalItems": 1
}
```

### 3. Busca de Cliente por ID

O endpoint recebe o parâmetro `customerId` via path.

*   **Exemplo de Chamada (cURL)**:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/customers/1' \
  -H 'accept: application/json'
```

*   **Exemplo de Retorno**:

```json
{
  "id": 1,
  "name": "Edson Cândido",
  "birthDate": "1975-03-09",
  "cpf": "12345678901",
  "email": "edd.candido@gmail.com",
  "phone": "11984173650",
  "createdAt": "2026-01-04T12:18:54-03:00",
  "updatedAt": "2026-01-04T12:18:54-03:00",
  "addresses": [
    {
      "id": 1,
      "street": "Estrada do Campo Limpo",
      "number": 5733,
      "complement": "Apto 42B Iguaçu",
      "zipcode": "05787000",
      "neighborhood": "Campo Limpo",
      "city": "São Paulo",
      "state": "SP",
      "country": "Brasil",
      "type": "RESIDENCIAL",
      "createdAt": "2026-01-04T12:18:54-03:00",
      "updatedAt": "2026-01-04T12:18:54-03:00"
    },
    {
      "id": 2,
      "street": "Av Copacabana",
      "number": 5000,
      "complement": "Bloco C, Apt 101",
      "zipcode": "22020001",
      "neighborhood": "Copacabana",
      "city": "Rio de Janeiro",
      "state": "RJ",
      "country": "Brasil",
      "type": "COMERCIAL",
      "createdAt": "2026-01-04T12:18:54-03:00",
      "updatedAt": "2026-01-04T12:18:54-03:00"
    }
  ]
}
```

### 4. Atualização Parcial de Cliente

O endpoint recebe o parâmetro `customerId` via path e um corpo com os dados parciais para serem atualizados (o campo `cpf` não é permitido alterar). Caso o e-mail seja alterado, o sistema validará se o novo e-mail já foi utilizado por outro cadastro.

*   **Exemplo de Chamada (cURL)**:

```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/v1/customers/1' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Edson Cândido da Silva",
  "birthDate": "1985-03-09",
  "email": "edd.candido@icloud.com",
  "phone": "11984173651"
}'
```

*   **Exemplo de Retorno**:

```json
{
  "id": 1,
  "name": "Edson Cândido da Silva",
  "birthDate": "1985-03-09",
  "cpf": "12345678901",
  "email": "edd.candido@icloud.com",
  "phone": "11984173651",
  "createdAt": "2026-01-04T12:18:54-03:00",
  "updatedAt": "2026-01-04T12:24:12-03:00"
}
```

### 5. Atualização Parcial de Endereço

O endpoint recebe o parâmetro `addressId` via path e um corpo com os dados parciais para serem atualizados.

*   **Exemplo de Chamada (cURL)**:

```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/v1/addresses/2' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "street": "Av. Goethe",
    "number": 5000,
    "complement": "Bloco C, Apt 101",
    "zipcode": "90035006",
    "neighborhood": "Moinhos de Vento",
    "city": "Porto Alegre",
    "state": "RS"
}'
```

*   **Exemplo de Retorno**:

```json
{
  "id": 2,
  "street": "Av. Goethe",
  "number": 5000,
  "complement": "Bloco C, Apt 101",
  "zipcode": "90035006",
  "neighborhood": "Moinhos de Vento",
  "city": "Porto Alegre",
  "state": "RS",
  "country": "Brasil",
  "type": "COMERCIAL",
  "createdAt": "2026-01-04T12:18:54-03:00",
  "updatedAt": "2026-01-04T12:26:02-03:00"
}
```
