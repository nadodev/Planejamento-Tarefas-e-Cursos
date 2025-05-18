package br.com.leonardo.planejador_horario.domain.exception;

public class DisponibilidadeNaoEncontradaException extends RuntimeException {
    
    public DisponibilidadeNaoEncontradaException(Long id) {
        super("Disponibilidade não encontrada com ID: " + id);
    }
} 