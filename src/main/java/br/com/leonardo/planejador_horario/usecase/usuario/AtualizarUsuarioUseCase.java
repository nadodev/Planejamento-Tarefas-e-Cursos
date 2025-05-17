package br.com.leonardo.planejador_horario.usecase.usuario;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import java.util.Optional;

public interface AtualizarUsuarioUseCase {
    Optional<UsuarioEntity> atualizarUsuario(Long id, UsuarioDTO usuarioDTO);
} 