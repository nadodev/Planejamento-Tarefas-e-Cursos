package br.com.leonardo.planejador_horario.domain.exception;

public class TarefaNaoEncontradaException extends RuntimeException {
    
    public TarefaNaoEncontradaException(Long id) {
        super("Tarefa não encontrada com ID: " + id);
    }
} 