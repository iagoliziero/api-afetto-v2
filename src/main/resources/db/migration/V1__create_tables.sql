-- =========================================================
-- AFETTO - MIGRATION V1
-- Criação inicial das tabelas
-- Adaptado para MySQL (BINARY(16) no lugar de UUID nativo,
-- conforme o mapeamento padrão do Hibernate para java.util.UUID
-- quando o banco não possui tipo UUID nativo)
-- =========================================================


-- =========================================================
-- PAÍS
-- =========================================================

CREATE TABLE TBL_PAIS (
                          id_pais BINARY(16) PRIMARY KEY,
                          nome VARCHAR(255),
                          sigla VARCHAR(255)
);


-- =========================================================
-- ESTADO
-- =========================================================

CREATE TABLE TBL_ESTADO (
                            id_estado BINARY(16) PRIMARY KEY,
                            nome VARCHAR(255),
                            sigla VARCHAR(255),

                            id_pais BINARY(16) NOT NULL,

                            CONSTRAINT fk_estado_pais
                                FOREIGN KEY (id_pais)
                                    REFERENCES TBL_PAIS(id_pais)
);


-- =========================================================
-- CIDADE
-- =========================================================

CREATE TABLE TBL_CIDADE (
                            id_cidade BINARY(16) PRIMARY KEY,
                            nome VARCHAR(255),

                            id_estado BINARY(16) NOT NULL,

                            CONSTRAINT fk_cidade_estado
                                FOREIGN KEY (id_estado)
                                    REFERENCES TBL_ESTADO(id_estado)
);


-- =========================================================
-- BAIRRO
-- =========================================================

CREATE TABLE TBL_BAIRRO (
                            id_bairro BINARY(16) PRIMARY KEY,
                            nome VARCHAR(255),

                            id_cidade BINARY(16) NOT NULL,

                            CONSTRAINT fk_bairro_cidade
                                FOREIGN KEY (id_cidade)
                                    REFERENCES TBL_CIDADE(id_cidade)
);


-- =========================================================
-- LOGRADOURO
-- =========================================================

CREATE TABLE TBL_LOGRADOURO (
                                id_logradouro BINARY(16) PRIMARY KEY,
                                nome VARCHAR(255),

                                id_bairro BINARY(16) NOT NULL,

                                CONSTRAINT fk_logradouro_bairro
                                    FOREIGN KEY (id_bairro)
                                        REFERENCES TBL_BAIRRO(id_bairro)
);


-- =========================================================
-- ENDEREÇO
-- =========================================================

CREATE TABLE TBL_ENDERECO (
                              id_endereco BINARY(16) PRIMARY KEY,
                              numero VARCHAR(255),
                              complemento VARCHAR(255),
                              cep VARCHAR(255),

                              latitude DECIMAL(10,7),
                              longitude DECIMAL(10,7),

                              id_logradouro BINARY(16) NOT NULL,

                              CONSTRAINT fk_endereco_logradouro
                                  FOREIGN KEY (id_logradouro)
                                      REFERENCES TBL_LOGRADOURO(id_logradouro)
);


-- =========================================================
-- USUÁRIO
-- =========================================================

CREATE TABLE TBL_USUARIO (
                             id_usuario BINARY(16) PRIMARY KEY,

                             nome VARCHAR(255),
                             cpf VARCHAR(255),
                             data_nascimento DATE,

                             email VARCHAR(255) NOT NULL UNIQUE,
                             senha VARCHAR(255) NOT NULL,

                             telefone VARCHAR(255),

                             role VARCHAR(255) NOT NULL,

                             id_endereco BINARY(16) UNIQUE,

                             CONSTRAINT fk_usuario_endereco
                                 FOREIGN KEY (id_endereco)
                                     REFERENCES TBL_ENDERECO(id_endereco)
);


-- =========================================================
-- PET
-- =========================================================

CREATE TABLE TBL_PET (
                         id BINARY(16) PRIMARY KEY,

                         nome VARCHAR(255),
                         especie VARCHAR(255),
                         raca VARCHAR(255),
                         sexo VARCHAR(255),

                         peso REAL,

                         data_nasc DATE,

                         descricao VARCHAR(255),

                         id_usuario BINARY(16),

                         CONSTRAINT fk_pet_usuario
                             FOREIGN KEY (id_usuario)
                                 REFERENCES TBL_USUARIO(id_usuario)
);


-- =========================================================
-- VETERINÁRIO
-- =========================================================

CREATE TABLE TBL_VETERINARIO (
                                 id_veterinario BINARY(16) PRIMARY KEY,

                                 nome VARCHAR(255),
                                 especialidade VARCHAR(255),
                                 crmv VARCHAR(255),
                                 telefone VARCHAR(255)
);


-- =========================================================
-- CLÍNICA
-- =========================================================

CREATE TABLE TBL_CLINICA (
                             id_clinica BINARY(16) PRIMARY KEY,

                             nome VARCHAR(255),
                             cnpj VARCHAR(255),
                             telefone VARCHAR(255),
                             email VARCHAR(255),

                             id_endereco BINARY(16) NOT NULL,

                             CONSTRAINT fk_clinica_endereco
                                 FOREIGN KEY (id_endereco)
                                     REFERENCES TBL_ENDERECO(id_endereco)
);


-- =========================================================
-- CLÍNICA X VETERINÁRIO
-- =========================================================

CREATE TABLE TBL_CLINICA_VETERINARIO (
                                         id BINARY(16) PRIMARY KEY,

                                         data_inicio DATE,
                                         turno VARCHAR(255),
                                         ativo BOOLEAN,

                                         id_clinica BINARY(16),
                                         id_veterinario BINARY(16),

                                         CONSTRAINT fk_clinica_veterinario_clinica
                                             FOREIGN KEY (id_clinica)
                                                 REFERENCES TBL_CLINICA(id_clinica),

                                         CONSTRAINT fk_clinica_veterinario_veterinario
                                             FOREIGN KEY (id_veterinario)
                                                 REFERENCES TBL_VETERINARIO(id_veterinario)
);


-- =========================================================
-- HISTÓRICO
-- =========================================================

CREATE TABLE TBL_HISTORICO (
                               id_historico BINARY(16) PRIMARY KEY,

                               data DATE,
                               tipo_evento VARCHAR(255),
                               descricao VARCHAR(255),
                               status VARCHAR(255),
                               observacoes VARCHAR(255),

                               id_pet BINARY(16),
                               id_veterinario BINARY(16),

                               CONSTRAINT fk_historico_pet
                                   FOREIGN KEY (id_pet)
                                       REFERENCES TBL_PET(id),

                               CONSTRAINT fk_historico_veterinario
                                   FOREIGN KEY (id_veterinario)
                                       REFERENCES TBL_VETERINARIO(id_veterinario)
);


-- =========================================================
-- VACINA
-- =========================================================

CREATE TABLE TBL_VACINA (
                            id_vacina BINARY(16) PRIMARY KEY,

                            nome_vacina VARCHAR(100) NOT NULL,
                            fabricante VARCHAR(100),
                            lote VARCHAR(50),

                            data_aplicacao DATE NOT NULL,
                            proxima_dose DATE,

                            observacoes TEXT,

                            id_pet BINARY(16),
                            id_historico BINARY(16),

                            CONSTRAINT fk_vacina_pet
                                FOREIGN KEY (id_pet)
                                    REFERENCES TBL_PET(id),

                            CONSTRAINT fk_vacina_historico
                                FOREIGN KEY (id_historico)
                                    REFERENCES TBL_HISTORICO(id_historico)
);
