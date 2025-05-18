package br.com.leonardo.planejador_horario.dto;

import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class DisponibilidadeRequest {
    
    @NotNull(message = "O dia da semana é obrigatório")
    private DiaSemana diaSemana;

    @NotNull(message = "A hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "A hora de fim é obrigatória")
    private LocalTime horaFim;

    @NotNull(message = "O campo preferencial é obrigatório")
    private Boolean preferencial;

    public DisponibilidadeRequest() {
    }

    public DisponibilidadeRequest(DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFim, Boolean preferencial) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.preferencial = preferencial;
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

    public Boolean getPreferencial() {
        return preferencial;
    }

    public void setPreferencial(Boolean preferencial) {
        this.preferencial = preferencial;
    }
} 