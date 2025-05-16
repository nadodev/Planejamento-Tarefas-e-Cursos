package br.com.leonardo.planejador_horario.usecase.curso;

import org.springframework.stereotype.Component;

@Component
public interface DeletaCursoUseCase {
    void deletar(Long id);
}
