-- Sequence para tabela de Clientes
CREATE SEQUENCE customer_id_seq START WITH 1 INCREMENT BY 50;

-- Tabela de Clientes
CREATE TABLE CUSTOMER
(
    id         BIGINT PRIMARY KEY           DEFAULT nextval('customer_id_seq'),
    name       VARCHAR(255)        NOT NULL,
    birth_date DATE                NOT NULL,
    cpf        VARCHAR(11) UNIQUE  NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    phone      VARCHAR(20)         NOT NULL,
    created_at TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Sequence para tabela de Endereços
CREATE SEQUENCE address_id_seq START WITH 1 INCREMENT BY 50;

-- Tabela de Endereços
CREATE TABLE ADDRESS
(
    id           BIGINT PRIMARY KEY    DEFAULT nextval('address_id_seq'),
    street       VARCHAR(255) NOT NULL,
    number       INTEGER      NOT NULL,
    complement   VARCHAR(255),
    neighborhood VARCHAR(100) NOT NULL,
    city         VARCHAR(100) NOT NULL,
    state        VARCHAR(2)   NOT NULL,
    zip_code     VARCHAR(20)  NOT NULL,
    country      VARCHAR(100) NOT NULL,
    type         VARCHAR(100) NOT NULL,
    customer_id  BIGINT       NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_address_customer
        FOREIGN KEY (customer_id)
            REFERENCES CUSTOMER (id)
            ON DELETE CASCADE
);