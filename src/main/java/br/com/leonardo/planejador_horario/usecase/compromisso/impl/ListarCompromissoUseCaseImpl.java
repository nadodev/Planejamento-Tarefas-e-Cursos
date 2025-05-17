package br.com.leonardo.planejador_horario.usecase.compromisso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CompromissoMapper;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.ListarCompromissoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarCompromissoUseCaseImpl implements ListarCompromissoUseCase {

    private final CompromissoRepository compromissoRepository;
    private final CompromissoMapper compromissoMapper;

    public ListarCompromissoUseCaseImpl(
            CompromissoRepository compromissoRepository,
            CompromissoMapper compromissoMapper
    ) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoMapper = compromissoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompromissoDTO> listarTodos() {
        return compromissoRepository.findAll().stream()
                .map(compromissoMapper::toDomain)
                .map(compromissoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompromissoDTO> listarPorUsuario(Long usuarioId) {
        return compromissoRepository.findByUsuarioId(usuarioId).stream()
                .map(compromissoMapper::toDomain)
                .map(compromissoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompromissoDTO buscarPorId(Long id) {
        CompromissoEntity compromisso = compromissoRepository.findById(id)
                .orElseThrow(() -> new CompromissoNaoEncontradoException("Compromisso não encontrado"));
        
        return compromissoMapper.toDTO(compromissoMapper.toDomain(compromisso));
    }
}
