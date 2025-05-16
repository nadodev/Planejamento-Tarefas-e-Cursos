package br.com.leonardo.planejador_horario.domain.exception;

public class CursoResponsavelObrigatorioException extends CursoException {
    public CursoResponsavelObrigatorioException() {
        super("Responsável pelo curso é obrigatório");
    }
}
