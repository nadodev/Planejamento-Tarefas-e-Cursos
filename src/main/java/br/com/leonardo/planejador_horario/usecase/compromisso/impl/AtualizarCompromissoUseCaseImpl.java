package br.com.leonardo.planejador_horario.usecase.compromisso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CompromissoMapper;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import br.com.leonardo.planejador_horario.application.port.out.UsuarioRepository;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.AtualizarCompromissoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarCompromissoUseCaseImpl implements AtualizarCompromissoUseCase {

    private final CompromissoRepository compromissoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CompromissoMapper compromissoMapper;

    public AtualizarCompromissoUseCaseImpl(
            CompromissoRepository compromissoRepository,
            UsuarioRepository usuarioRepository,
            CompromissoMapper compromissoMapper
    ) {
        this.compromissoRepository = compromissoRepository;
        this.usuarioRepository = usuarioRepository;
        this.compromissoMapper = compromissoMapper;
    }

    @Override
    @Transactional
    public CompromissoDTO atualizar(Long id, CompromissoDTO compromissoDTO) {
        CompromissoEntity compromissoExistente = compromissoRepository.findById(id)
                .orElseThrow(() -> new CompromissoNaoEncontradoException("Compromisso não encontrado"));

        UsuarioEntity usuario = usuarioRepository.findById(compromissoDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        compromissoExistente.setTitulo(compromissoDTO.getTitulo());
        compromissoExistente.setDiaDaSemana(compromissoDTO.getDiaDaSemana());
        compromissoExistente.setHorarioInicio(compromissoDTO.getHorarioInicio());
        compromissoExistente.setHorarioFim(compromissoDTO.getHorarioFim());
        compromissoExistente.setRecorrente(compromissoDTO.isRecorrente());
        compromissoExistente.setUsuario(usuario);

        CompromissoEntity compromissoAtualizado = compromissoRepository.save(compromissoExistente);
        return compromissoMapper.toDTO(compromissoMapper.toDomain(compromissoAtualizado));
    }
} 