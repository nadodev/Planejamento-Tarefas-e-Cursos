package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.DisponibilidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<DisponibilidadeEntity, Long> {
    List<DisponibilidadeEntity> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT COUNT(d) FROM DisponibilidadeEntity d WHERE d.usuario.id = :usuarioId AND d.diaSemana = :diaSemana AND d.preferencial = true")
    long countPreferenciaisPorDia(@Param("usuarioId") Long usuarioId, @Param("diaSemana") DayOfWeek diaSemana);
} 