package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.TarefaEntity;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaEntity, Long> {
    
    Page<TarefaEntity> findByUsuarioId(Long usuarioId, Pageable pageable);
    
    List<TarefaEntity> findByUsuarioIdAndStatus(Long usuarioId, Status status);
    
    List<TarefaEntity> findByUsuarioIdAndPrioridade(Long usuarioId, Prioridade prioridade);
    
    List<TarefaEntity> findByUsuarioIdAndCategoria(Long usuarioId, String categoria);
    
    @Query("SELECT t FROM TarefaEntity t WHERE t.usuario.id = :usuarioId AND t.prazo BETWEEN :dataInicio AND :dataFim")
    List<TarefaEntity> findByUsuarioIdAndPrazoBetween(
        @Param("usuarioId") Long usuarioId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
    
    @Query("SELECT t FROM TarefaEntity t WHERE t.usuario.id = :usuarioId AND t.prazo < :hoje AND t.status != 'CONCLUIDA'")
    List<TarefaEntity> findTarefasAtrasadas(
        @Param("usuarioId") Long usuarioId,
        @Param("hoje") LocalDate hoje
    );
    
    @Query("SELECT t FROM TarefaEntity t WHERE t.usuario.id = :usuarioId AND t.prazo = :hoje AND t.status != 'CONCLUIDA'")
    List<TarefaEntity> findTarefasParaHoje(
        @Param("usuarioId") Long usuarioId,
        @Param("hoje") LocalDate hoje
    );
} 