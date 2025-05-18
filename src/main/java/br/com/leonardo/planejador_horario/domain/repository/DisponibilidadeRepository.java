package br.com.leonardo.planejador_horario.domain.repository;

import br.com.leonardo.planejador_horario.domain.entities.Disponibilidade;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    List<Disponibilidade> findByUsuarioId(Long usuarioId);
    List<Disponibilidade> findByUsuarioIdAndDiaSemana(Long usuarioId, DiaSemana diaSemana);
} 