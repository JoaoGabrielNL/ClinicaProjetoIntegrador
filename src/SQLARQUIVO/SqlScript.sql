CREATE DATABASE IF NOT EXISTS clinica;

USE clinica;

CREATE TABLE IF NOT EXISTS especialidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS convenio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    data_nascimento DATE,          
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT 1 
);

CREATE TABLE IF NOT EXISTS portal_paciente (
    paciente_id BIGINT PRIMARY KEY,        
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,            
    FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS atendimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    data_registro DATE NOT NULL,        
    anamnese TEXT,
    diagnostico TEXT,
    conduta TEXT,
    medico_responsavel VARCHAR(100),        
    FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE RESTRICT
);

CREATE TABLE medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE, -- O CRM é obrigatório e único
    telefone VARCHAR(20),
    email VARCHAR(100),
    especialidade_id BIGINT,
    FOREIGN KEY (especialidade_id) 
        REFERENCES especialidade(id) 
        ON DELETE SET NULL 
);

CREATE TABLE IF NOT EXISTS consulta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_consulta DATE NOT NULL,     
    hora_consulta VARCHAR(5) NOT NULL, 
    observacoes TEXT,
    convenio_id BIGINT NULL,
    realizada TINYINT(1) DEFAULT 0 NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE RESTRICT,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE RESTRICT,
    FOREIGN KEY (convenio_id) REFERENCES convenio(id) ON DELETE SET NULL
);