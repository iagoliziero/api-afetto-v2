-- =========================================================
-- AFETTO - SCRIPT DO BANCO DE DADOS (script_bd.sql)
-- FIAP 2026 — DevOps Tools & Cloud Computing — Sprint 3
-- =========================================================
-- Este script documenta a estrutura das tabelas do CORE da
-- aplicação Afetto — o domínio de saúde animal (tutores,
-- pets, vacinas, veterinários e clínicas).
--
-- Banco de dados: MySQL 8.0
-- Tipo utilizado para chaves primárias/estrangeiras: BINARY(16)
-- (mapeamento padrão do Hibernate para java.util.UUID quando o
-- banco não possui tipo UUID nativo, como é o caso do MySQL)
-- =========================================================


-- =========================================================
-- TABELA: TBL_USUARIO
-- Representa o tutor do pet — dono da conta na plataforma.
-- =========================================================

CREATE TABLE TBL_USUARIO (
    id_usuario       BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único do usuário (UUID)',
    nome             VARCHAR(255)  COMMENT 'Nome completo do tutor',
    cpf              VARCHAR(255)  COMMENT 'CPF do tutor',
    data_nascimento  DATE          COMMENT 'Data de nascimento do tutor',
    email            VARCHAR(255)  NOT NULL UNIQUE COMMENT 'E-mail do tutor, utilizado como login',
    senha            VARCHAR(255)  NOT NULL COMMENT 'Senha do tutor, armazenada com hash',
    telefone         VARCHAR(255)  COMMENT 'Telefone de contato do tutor',
    role             VARCHAR(255)  NOT NULL COMMENT 'Papel de acesso do usuário: ADMIN ou USER'
) COMMENT='Tutores cadastrados na plataforma Afetto';


-- =========================================================
-- TABELA: TBL_PET
-- Representa o animal de estimação vinculado a um tutor.
-- Entidade central do domínio da aplicação.
-- =========================================================

CREATE TABLE TBL_PET (
    id           BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único do pet (UUID)',
    nome         VARCHAR(255)  COMMENT 'Nome do pet',
    especie      VARCHAR(255)  COMMENT 'Espécie do pet: CACHORRO, GATO, COELHO, AVE, REPTIL, ROEDOR, PORCO, MACACO, CAVALO, PEIXE, INSETO, OUTRO',
    raca         VARCHAR(255)  COMMENT 'Raça do pet',
    sexo         VARCHAR(255)  COMMENT 'Sexo do pet: MACHO ou FEMEA',
    peso         REAL          COMMENT 'Peso do pet em quilogramas',
    data_nasc    DATE          COMMENT 'Data de nascimento do pet',
    descricao    VARCHAR(255)  COMMENT 'Observações gerais sobre o pet',
    id_usuario   BINARY(16)    COMMENT 'Referência ao tutor responsável pelo pet',

    CONSTRAINT fk_pet_usuario
        FOREIGN KEY (id_usuario)
            REFERENCES TBL_USUARIO(id_usuario)
) COMMENT='Pets cadastrados, vinculados a um tutor (TBL_USUARIO)';


-- =========================================================
-- TABELA: TBL_VETERINARIO
-- Profissional responsável pelo atendimento clínico do pet.
-- =========================================================

CREATE TABLE TBL_VETERINARIO (
    id_veterinario  BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único do veterinário (UUID)',
    nome            VARCHAR(255)  COMMENT 'Nome completo do veterinário',
    especialidade   VARCHAR(255)  COMMENT 'Área de especialidade do veterinário',
    crmv            VARCHAR(255)  COMMENT 'Registro profissional (CRMV)',
    telefone        VARCHAR(255)  COMMENT 'Telefone de contato do veterinário'
) COMMENT='Veterinários responsáveis pelos atendimentos';


-- =========================================================
-- TABELA: TBL_CLINICA
-- Clínica veterinária parceira da plataforma.
-- =========================================================

CREATE TABLE TBL_CLINICA (
    id_clinica  BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único da clínica (UUID)',
    nome        VARCHAR(255)  COMMENT 'Nome da clínica veterinária',
    cnpj        VARCHAR(255)  COMMENT 'CNPJ da clínica',
    telefone    VARCHAR(255)  COMMENT 'Telefone de contato da clínica',
    email       VARCHAR(255)  COMMENT 'E-mail de contato da clínica'
) COMMENT='Clínicas veterinárias parceiras da plataforma Afetto';


-- =========================================================
-- TABELA: TBL_CLINICA_VETERINARIO
-- Associação entre clínicas e veterinários (N:N),
-- registrando o vínculo de trabalho e o turno de atuação.
-- =========================================================

CREATE TABLE TBL_CLINICA_VETERINARIO (
    id              BINARY(16)  NOT NULL PRIMARY KEY COMMENT 'Identificador único do vínculo (UUID)',
    data_inicio     DATE        COMMENT 'Data de início do vínculo entre veterinário e clínica',
    turno           VARCHAR(255) COMMENT 'Turno de atuação do veterinário na clínica',
    ativo           BOOLEAN     COMMENT 'Indica se o vínculo está ativo',
    id_clinica      BINARY(16)  COMMENT 'Referência à clínica',
    id_veterinario  BINARY(16)  COMMENT 'Referência ao veterinário',

    CONSTRAINT fk_clinica_veterinario_clinica
        FOREIGN KEY (id_clinica)
            REFERENCES TBL_CLINICA(id_clinica),

    CONSTRAINT fk_clinica_veterinario_veterinario
        FOREIGN KEY (id_veterinario)
            REFERENCES TBL_VETERINARIO(id_veterinario)
) COMMENT='Vínculo de trabalho entre veterinários e clínicas';


-- =========================================================
-- TABELA: TBL_HISTORICO
-- Registro de eventos clínicos do pet (consultas, exames,
-- procedimentos), associado a um veterinário responsável.
-- =========================================================

CREATE TABLE TBL_HISTORICO (
    id_historico    BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único do registro histórico (UUID)',
    data            DATE          COMMENT 'Data do evento clínico',
    tipo_evento     VARCHAR(255)  COMMENT 'Tipo do evento: consulta, exame, procedimento etc.',
    descricao       VARCHAR(255)  COMMENT 'Descrição do evento clínico',
    status          VARCHAR(255)  COMMENT 'Status do evento (ex.: concluído, pendente)',
    observacoes     VARCHAR(255)  COMMENT 'Observações adicionais do veterinário',
    id_pet          BINARY(16)    COMMENT 'Referência ao pet atendido',
    id_veterinario  BINARY(16)    COMMENT 'Referência ao veterinário responsável pelo atendimento',

    CONSTRAINT fk_historico_pet
        FOREIGN KEY (id_pet)
            REFERENCES TBL_PET(id),

    CONSTRAINT fk_historico_veterinario
        FOREIGN KEY (id_veterinario)
            REFERENCES TBL_VETERINARIO(id_veterinario)
) COMMENT='Histórico de atendimentos clínicos do pet';


-- =========================================================
-- TABELA: TBL_VACINA
-- Registro de vacinação aplicada a um pet, podendo estar
-- vinculado a um registro do histórico clínico.
-- =========================================================

CREATE TABLE TBL_VACINA (
    id_vacina        BINARY(16)    NOT NULL PRIMARY KEY COMMENT 'Identificador único da vacina aplicada (UUID)',
    nome_vacina      VARCHAR(100)  NOT NULL COMMENT 'Nome da vacina aplicada',
    fabricante       VARCHAR(100)  COMMENT 'Fabricante da vacina',
    lote             VARCHAR(50)   COMMENT 'Número do lote da vacina',
    data_aplicacao   DATE          NOT NULL COMMENT 'Data em que a vacina foi aplicada',
    proxima_dose     DATE          COMMENT 'Data prevista para a próxima dose, se houver',
    observacoes      TEXT          COMMENT 'Observações sobre a aplicação da vacina',
    id_pet           BINARY(16)    COMMENT 'Referência ao pet vacinado',
    id_historico     BINARY(16)    COMMENT 'Referência ao registro de histórico associado, se houver',

    CONSTRAINT fk_vacina_pet
        FOREIGN KEY (id_pet)
            REFERENCES TBL_PET(id),

    CONSTRAINT fk_vacina_historico
        FOREIGN KEY (id_historico)
            REFERENCES TBL_HISTORICO(id_historico)
) COMMENT='Registro de vacinas aplicadas aos pets';