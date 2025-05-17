package br.com.leonardo.planejador_horario.application.port.out;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    Optional<UsuarioEntity> findById(Long id);
    Optional<UsuarioEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteById(Long id);
    boolean existsById(Long id);
} 