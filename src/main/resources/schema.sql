-- Desabilita temporariamente as restrições de chave estrangeira
SET FOREIGN_KEY_CHECKS = 0;

-- Primeiro, remova a tabela dependente (cursos)
DROP TABLE IF EXISTS cursos;

-- Depois, remova a tabela referenciada (usuarios)
DROP TABLE IF EXISTS usuarios;

-- Reabilita as restrições
SET FOREIGN_KEY_CHECKS = 1;

-- Criação da tabela de usuários
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NULL DEFAULT NULL
);

-- Criação da tabela de cursos com a foreign key para usuarios
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    carga_horaria INTEGER NOT NULL,
    prioridade INTEGER NOT NULL,
    prazo_final DATE NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
