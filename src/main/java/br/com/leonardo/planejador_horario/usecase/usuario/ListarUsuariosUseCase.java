package br.com.leonardo.planejador_horario.usecase.usuario;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import java.util.List;
import java.util.Optional;

public interface ListarUsuariosUseCase {
    List<UsuarioEntity> listarUsuarios();
    Optional<UsuarioEntity> buscarPorId(Long id);
    Optional<UsuarioEntity> buscarPorEmail(String email);
} 