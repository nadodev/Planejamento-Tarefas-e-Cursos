package br.com.leonardo.planejador_horario.usecase.curso;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import jakarta.validation.Valid;
import org.springframework.stereotype.Repository;

@Repository
public interface CriarCursoUseCase {
    Curso criar(@Valid CursoDTO curso);
}