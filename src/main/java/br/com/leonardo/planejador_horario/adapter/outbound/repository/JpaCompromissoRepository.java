package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCompromissoRepository extends JpaRepository<CompromissoEntity, Long> {
    List<CompromissoEntity> findByUsuarioId(Long usuarioId);
}
