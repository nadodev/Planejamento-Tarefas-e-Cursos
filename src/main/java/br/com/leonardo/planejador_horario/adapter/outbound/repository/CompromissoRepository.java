package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompromissoRepository  extends JpaRepository<CompromissoEntity, Long> {
}
