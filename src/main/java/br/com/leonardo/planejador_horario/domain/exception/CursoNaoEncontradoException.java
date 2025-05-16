package br.com.leonardo.planejador_horario.domain.exception;

public class CursoNaoEncontradoException extends CursoException {
    public CursoNaoEncontradoException(Long id) {
        super("Curso com ID " + id + " não encontrado");
    }
}
