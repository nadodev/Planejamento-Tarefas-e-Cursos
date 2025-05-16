package br.com.leonardo.planejador_horario.domain.exception;

public class UsuarioNaoEncontradoException extends UsuarioException {
    public UsuarioNaoEncontradoException(Iterable<Long> id) {
        super("Usuario nao encontrado");
    }
}
