package br.com.leonardo.planejador_horario.usecase.compromisso;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import java.util.List;

public interface ListarCompromissoUseCase {
    List<CompromissoDTO> listarTodos();
    List<CompromissoDTO> listarPorUsuario(Long usuarioId);
    CompromissoDTO buscarPorId(Long id);
}
