package br.com.leonardo.planejador_horario.usecase.curso;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import jakarta.validation.Valid;

public interface CriarCursoUseCase {
    CursoDTO criar(@Valid CursoDTO cursoDTO);
}