package br.com.leonardo.planejador_horario.usecase.compromisso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import br.com.leonardo.planejador_horario.application.port.out.UsuarioRepository;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.CriarCompromissoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarCompromissoUseCaseImpl implements CriarCompromissoUseCase {

    private final CompromissoRepository compromissoRepository;
    private final UsuarioRepository usuarioRepository;

    public CriarCompromissoUseCaseImpl(
            CompromissoRepository compromissoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.compromissoRepository = compromissoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public CompromissoDTO criar(CompromissoDTO compromissoDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(compromissoDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        CompromissoEntity compromisso = new CompromissoEntity();
        compromisso.setTitulo(compromissoDTO.getTitulo());
        compromisso.setDiaDaSemana(compromissoDTO.getDiaDaSemana());
        compromisso.setHorarioInicio(compromissoDTO.getHorarioInicio());
        compromisso.setHorarioFim(compromissoDTO.getHorarioFim());
        compromisso.setRecorrente(compromissoDTO.isRecorrente());
        compromisso.setUsuario(usuario);

        CompromissoEntity compromissoSalvo = compromissoRepository.save(compromisso);

        return new CompromissoDTO(
                compromissoSalvo.getId(),
                compromissoSalvo.getTitulo(),
                compromissoSalvo.getDiaDaSemana(),
                compromissoSalvo.getHorarioInicio(),
                compromissoSalvo.getHorarioFim(),
                compromissoSalvo.isRecorrente(),
                compromissoSalvo.getUsuario().getId()
        );
    }
}
