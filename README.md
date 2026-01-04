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
