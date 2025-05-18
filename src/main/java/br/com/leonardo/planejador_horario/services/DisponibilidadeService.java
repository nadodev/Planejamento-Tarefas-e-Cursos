package br.com.leonardo.planejador_horario.services;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.DisponibilidadeEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.DisponibilidadeRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.UsuarioRepository;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.domain.exception.ResourceNotFoundException;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadeService {

    private final DisponibilidadeRepository disponibilidadeRepository;
    private final UsuarioRepository usuarioRepository;
    
    private static final LocalTime HORA_INICIO_FUNCIONAMENTO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIM_FUNCIONAMENTO = LocalTime.of(18, 0);

    @Autowired
    public DisponibilidadeService(DisponibilidadeRepository disponibilidadeRepository, UsuarioRepository usuarioRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public DisponibilidadeResponse criar(Long usuarioId, DisponibilidadeRequest request) {
        DisponibilidadeEntity disponibilidade = criarDisponibilidade(
            usuarioId,
            request.getDiaSemana().toDayOfWeek(),
            request.getHoraInicio(),
            request.getHoraFim(),
            request.getPreferencial()
        );
        return toResponse(disponibilidade);
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadeResponse> listarPorUsuario(Long usuarioId) {
        return disponibilidadeRepository.findByUsuarioId(usuarioId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadeResponse> listarPorUsuarioEDia(Long usuarioId, DiaSemana diaSemana) {
        return disponibilidadeRepository.findByUsuarioId(usuarioId)
            .stream()
            .filter(d -> d.getDiaSemana() == diaSemana.toDayOfWeek())
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void excluir(Long id) {
        excluirDisponibilidade(id);
    }

    private DisponibilidadeEntity criarDisponibilidade(Long usuarioId, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFim, boolean preferencial) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        validarHorarios(horaInicio, horaFim);
        validarHorarioFuncionamento(horaInicio, horaFim);
        
        if (preferencial) {
            validarPreferencialUnico(usuarioId, diaSemana);
        }

        DisponibilidadeEntity disponibilidade = new DisponibilidadeEntity();
        disponibilidade.setUsuario(usuario);
        disponibilidade.setDiaSemana(diaSemana);
        disponibilidade.setHoraInicio(horaInicio);
        disponibilidade.setHoraFim(horaFim);
        disponibilidade.setPreferencial(preferencial);

        return disponibilidadeRepository.save(disponibilidade);
    }

    private void excluirDisponibilidade(Long id) {
        if (!disponibilidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disponibilidade não encontrada");
        }
        disponibilidadeRepository.deleteById(id);
    }

    private void validarHorarios(LocalTime inicio, LocalTime fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A hora de início deve ser anterior à hora de fim");
        }
    }

    private void validarHorarioFuncionamento(LocalTime inicio, LocalTime fim) {
        if (inicio.isBefore(HORA_INICIO_FUNCIONAMENTO) || fim.isAfter(HORA_FIM_FUNCIONAMENTO)) {
            throw new IllegalArgumentException("Os horários devem estar dentro do horário de funcionamento (8h às 18h)");
        }
    }

    private void validarPreferencialUnico(Long usuarioId, DayOfWeek diaSemana) {
        long count = disponibilidadeRepository.countPreferenciaisPorDia(usuarioId, diaSemana);
        if (count > 0) {
            throw new IllegalStateException("Já existe uma disponibilidade preferencial para este dia");
        }
    }

    private DisponibilidadeResponse toResponse(DisponibilidadeEntity entity) {
        return new DisponibilidadeResponse(
            entity.getId(),
            entity.getUsuario().getId(),
            DiaSemana.fromDayOfWeek(entity.getDiaSemana()),
            entity.getHoraInicio(),
            entity.getHoraFim(),
            entity.isPreferencial()
        );
    }
} 