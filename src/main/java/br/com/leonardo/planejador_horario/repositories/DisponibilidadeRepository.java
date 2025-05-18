package br.com.leonardo.planejador_horario.repositories;

import br.com.leonardo.planejador_horario.domain.entities.Disponibilidade;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    
    List<Disponibilidade> findByUsuarioId(Long usuarioId);
    
    List<Disponibilidade> findByUsuarioIdAndDiaSemana(Long usuarioId, DiaSemana diaSemana);
    
    @Query("SELECT d FROM Disponibilidade d WHERE d.usuario.id = :usuarioId AND d.diaSemana = :diaSemana " +
           "AND ((d.horaInicio <= :horaFim AND d.horaFim >= :horaInicio) OR " +
           "(d.horaInicio >= :horaInicio AND d.horaInicio < :horaFim) OR " +
           "(d.horaFim > :horaInicio AND d.horaFim <= :horaFim))")
    List<Disponibilidade> findSobreposicoes(
        @Param("usuarioId") Long usuarioId,
        @Param("diaSemana") DiaSemana diaSemana,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFim") LocalTime horaFim
    );
    
    @Query("SELECT COUNT(d) FROM Disponibilidade d WHERE d.usuario.id = :usuarioId " +
           "AND d.diaSemana = :diaSemana AND d.preferencial = true")
    long countPreferenciaisPorDia(
        @Param("usuarioId") Long usuarioId,
        @Param("diaSemana") DiaSemana diaSemana
    );
} 