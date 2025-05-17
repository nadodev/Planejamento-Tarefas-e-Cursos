package br.com.leonardo.planejador_horario.usecase.compromisso.impl;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.DeletarCompromissoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeletarCompromissoUseCaseImpl implements DeletarCompromissoUseCase {

    private final CompromissoRepository compromissoRepository;

    public DeletarCompromissoUseCaseImpl(CompromissoRepository compromissoRepository) {
        this.compromissoRepository = compromissoRepository;
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        CompromissoEntity compromisso = compromissoRepository.findById(id)
                .orElseThrow(() -> new CompromissoNaoEncontradoException("Compromisso não encontrado"));

        compromissoRepository.delete(compromisso);
    }
} 