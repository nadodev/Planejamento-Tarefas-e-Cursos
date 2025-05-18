package br.com.leonardo.planejador_horario.dto;

import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;

import java.time.LocalTime;

public class DisponibilidadeResponse {
    private Long id;
    private Long usuarioId;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private boolean preferencial;

    public DisponibilidadeResponse() {
    }

    public DisponibilidadeResponse(Long id, Long usuarioId, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim, boolean preferencial) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.preferencial = preferencial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public boolean isPreferencial() {
        return preferencial;
    }

    public void setPreferencial(boolean preferencial) {
        this.preferencial = preferencial;
    }
} 