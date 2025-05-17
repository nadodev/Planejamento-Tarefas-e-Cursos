package br.com.leonardo.planejador_horario.usecase.usuario;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;

import java.util.Optional;

public interface UsuarioRepository {
    Optional<UsuarioEntity> findByEmail(String email);
    void deleteAll();
    UsuarioEntity save(UsuarioEntity usuario);
} 