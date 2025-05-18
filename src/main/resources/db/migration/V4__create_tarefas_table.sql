CREATE TABLE IF NOT EXISTS tarefas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    prazo DATE NOT NULL,
    prioridade VARCHAR(20) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    tempo_estimado INTEGER NOT NULL COMMENT 'Tempo estimado em minutos',
    status VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT chk_prioridade CHECK (prioridade IN ('ALTA', 'MEDIA', 'BAIXA')),
    CONSTRAINT chk_status CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA'))
); 