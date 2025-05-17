package br.com.leonardo.planejador_horario.usecase.compromisso;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import jakarta.validation.Valid;

public interface CriarCompromissoUseCase {
    CompromissoDTO criar(@Valid CompromissoDTO compromissoDTO);
}
