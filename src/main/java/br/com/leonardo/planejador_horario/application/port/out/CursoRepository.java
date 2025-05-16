package br.com.leonardo.planejador_horario.application.port.out;


import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;

import java.util.Optional;

public interface CursoRepository {
    Optional<CursoEntity> findById(Long id);
    void delete(CursoEntity curso);
}
