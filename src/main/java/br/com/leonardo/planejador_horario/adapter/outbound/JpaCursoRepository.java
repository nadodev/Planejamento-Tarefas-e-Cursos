package br.com.leonardo.planejador_horario.adapter.outbound;


import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCursoRepository extends JpaRepository<CursoEntity, Long> {
    List<CursoEntity> findByUsuarioId(Long usuarioId);
}