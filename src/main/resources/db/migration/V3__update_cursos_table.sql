-- Adiciona novas colunas à tabela cursos
ALTER TABLE cursos
ADD COLUMN plataforma VARCHAR(50) NOT NULL COMMENT 'Plataforma do curso (Alura, Udemy, etc)',
ADD COLUMN link VARCHAR(255) NULL COMMENT 'Link do curso (opcional)',
ADD COLUMN nivel VARCHAR(20) NOT NULL COMMENT 'Nível do curso (INICIANTE, INTERMEDIARIO, AVANCADO)',
ADD CONSTRAINT chk_nivel CHECK (nivel IN ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO')); 