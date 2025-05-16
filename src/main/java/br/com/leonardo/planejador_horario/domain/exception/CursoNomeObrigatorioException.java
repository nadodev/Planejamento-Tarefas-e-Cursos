package br.com.leonardo.planejador_horario.domain.exception;

public class CursoNomeObrigatorioException extends CursoException {
    public CursoNomeObrigatorioException() {
        super("Nome do curso é obrigatório");
    }
}
