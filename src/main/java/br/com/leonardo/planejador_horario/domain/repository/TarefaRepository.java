package br.com.leonardo.planejador_horario.domain.repository;

import br.com.leonardo.planejador_horario.domain.entities.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuarioId(Long usuarioId);
    Page<Tarefa> findByUsuarioId(Long usuarioId, Pageable pageable);
    List<Tarefa> findByUsuarioIdAndDataInicioBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
    List<Tarefa> findByUsuarioIdAndStatus(Long usuarioId, Tarefa.Status status);
    List<Tarefa> findByUsuarioIdAndPrioridade(Long usuarioId, Tarefa.Prioridade prioridade);
    List<Tarefa> findByUsuarioIdAndCategoria(Long usuarioId, String categoria);
    List<Tarefa> findByUsuarioIdAndDataFimBeforeAndStatusNot(Long usuarioId, LocalDate data, Tarefa.Status status);
    List<Tarefa> findByUsuarioIdAndDataInicio(Long usuarioId, LocalDate dataInicio);
} 