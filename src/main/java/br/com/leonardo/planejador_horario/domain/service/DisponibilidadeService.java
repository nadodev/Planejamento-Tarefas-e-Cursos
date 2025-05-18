package br.com.leonardo.planejador_horario.domain.service;

import br.com.leonardo.planejador_horario.domain.entities.Disponibilidade;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.domain.exception.DisponibilidadeNaoEncontradaException;
import br.com.leonardo.planejador_horario.domain.repository.DisponibilidadeRepository;
import br.com.leonardo.planejador_horario.domain.repository.UsuarioRepository;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public DisponibilidadeResponse criar(Long usuarioId, DisponibilidadeRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setUsuario(usuario);
        disponibilidade.setDiaSemana(request.getDiaSemana());
        disponibilidade.setHoraInicio(request.getHoraInicio());
        disponibilidade.setHoraFim(request.getHoraFim());
        Disponibilidade salvo = disponibilidadeRepository.save(disponibilidade);
        return toResponse(salvo);
    }

    public List<DisponibilidadeResponse> listarPorUsuario(Long usuarioId) {
        return disponibilidadeRepository.findByUsuarioId(usuarioId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<DisponibilidadeResponse> listarPorUsuarioEDia(Long usuarioId, DiaSemana diaSemana) {
        return disponibilidadeRepository.findByUsuarioIdAndDiaSemana(usuarioId, diaSemana)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void excluir(Long id) {
        Disponibilidade disponibilidade = disponibilidadeRepository.findById(id)
                .orElseThrow(() -> new DisponibilidadeNaoEncontradaException(id));
        disponibilidadeRepository.delete(disponibilidade);
    }

    // Conversor de entidade para DTO de resposta
    private DisponibilidadeResponse toResponse(Disponibilidade disponibilidade) {
        DisponibilidadeResponse response = new DisponibilidadeResponse();
        response.setId(disponibilidade.getId());
        response.setDiaSemana(disponibilidade.getDiaSemana());
        response.setHoraInicio(disponibilidade.getHoraInicio());
        response.setHoraFim(disponibilidade.getHoraFim());
        if (disponibilidade.getUsuario() != null) {
            response.setUsuarioId(disponibilidade.getUsuario().getId());
        }
        return response;
    }
} 