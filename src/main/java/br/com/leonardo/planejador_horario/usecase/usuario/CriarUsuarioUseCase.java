package br.com.leonardo.planejador_horario.usecase.usuario;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;

public interface CriarUsuarioUseCase {
    UsuarioEntity criarUsuario(UsuarioDTO usuarioDTO);
} 