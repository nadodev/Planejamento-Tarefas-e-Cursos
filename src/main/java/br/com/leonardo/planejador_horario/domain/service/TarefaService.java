package br.com.leonardo.planejador_horario.domain.service;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.TarefaDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.TarefaEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.TarefaRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.UsuarioRepository;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TarefaDTO criarTarefa(TarefaDTO tarefaDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(tarefaDTO.getUsuarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        TarefaEntity tarefa = new TarefaEntity();
        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setPrazo(tarefaDTO.getPrazo());
        tarefa.setPrioridade(tarefaDTO.getPrioridade());
        tarefa.setCategoria(tarefaDTO.getCategoria());
        tarefa.setTempoEstimado(tarefaDTO.getTempoEstimado());
        tarefa.setStatus(tarefaDTO.getStatus());
        tarefa.setUsuario(usuario);

        TarefaEntity tarefaSalva = tarefaRepository.save(tarefa);
        return converterParaDTO(tarefaSalva);
    }

    @Transactional(readOnly = true)
    public Page<TarefaDTO> listarTarefas(Long usuarioId, Pageable pageable) {
        return tarefaRepository.findByUsuarioId(usuarioId, pageable)
            .map(this::converterParaDTO);
    }

    @Transactional(readOnly = true)
    public TarefaDTO buscarTarefaPorId(Long id) {
        TarefaEntity tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
        return converterParaDTO(tarefa);
    }

    @Transactional
    public TarefaDTO atualizarTarefa(Long id, TarefaDTO tarefaDTO) {
        TarefaEntity tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

        tarefa.setTitulo(tarefaDTO.getTitulo());
        tarefa.setDescricao(tarefaDTO.getDescricao());
        tarefa.setPrazo(tarefaDTO.getPrazo());
        tarefa.setPrioridade(tarefaDTO.getPrioridade());
        tarefa.setCategoria(tarefaDTO.getCategoria());
        tarefa.setTempoEstimado(tarefaDTO.getTempoEstimado());
        tarefa.setStatus(tarefaDTO.getStatus());

        TarefaEntity tarefaAtualizada = tarefaRepository.save(tarefa);
        return converterParaDTO(tarefaAtualizada);
    }

    @Transactional
    public void excluirTarefa(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa não encontrada");
        }
        tarefaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasPorStatus(Long usuarioId, Status status) {
        return tarefaRepository.findByUsuarioIdAndStatus(usuarioId, status)
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasPorPrioridade(Long usuarioId, Prioridade prioridade) {
        return tarefaRepository.findByUsuarioIdAndPrioridade(usuarioId, prioridade)
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasPorCategoria(Long usuarioId, String categoria) {
        return tarefaRepository.findByUsuarioIdAndCategoria(usuarioId, categoria)
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasPorPeriodo(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        return tarefaRepository.findByUsuarioIdAndPrazoBetween(usuarioId, dataInicio, dataFim)
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasAtrasadas(Long usuarioId) {
        return tarefaRepository.findTarefasAtrasadas(usuarioId, LocalDate.now())
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaDTO> buscarTarefasParaHoje(Long usuarioId) {
        return tarefaRepository.findTarefasParaHoje(usuarioId, LocalDate.now())
            .stream()
            .map(this::converterParaDTO)
            .toList();
    }

    private TarefaDTO converterParaDTO(TarefaEntity tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setPrazo(tarefa.getPrazo());
        dto.setPrioridade(tarefa.getPrioridade());
        dto.setCategoria(tarefa.getCategoria());
        dto.setTempoEstimado(tarefa.getTempoEstimado());
        dto.setStatus(tarefa.getStatus());
        dto.setUsuarioId(tarefa.getUsuario().getId());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setDataAtualizacao(tarefa.getDataAtualizacao());
        return dto;
    }
} 