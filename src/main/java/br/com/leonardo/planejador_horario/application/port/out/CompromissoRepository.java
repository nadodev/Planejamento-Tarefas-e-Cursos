package br.com.leonardo.planejador_horario.application.port.out;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;

import java.util.List;
import java.util.Optional;

public interface CompromissoRepository {
    CompromissoEntity save(CompromissoEntity compromisso);
    Optional<CompromissoEntity> findById(Long id);
    List<CompromissoEntity> findAll();
    List<CompromissoEntity> findByUsuarioId(Long usuarioId);
    void delete(CompromissoEntity compromisso);
    void deleteById(Long id);
} 