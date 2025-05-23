package br.com.leonardo.planejador_horario.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Compromisso {
    private Long id;
    private String titulo;
    private Usuario usuario;
    private DayOfWeek diaDaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private boolean recorrente;

    public Compromisso(Long id, String titulo, Usuario usuario, DayOfWeek diaDaSemana, LocalTime horarioInicio, LocalTime horarioFim, boolean recorrente) {
        this.id = id;
        this.titulo = titulo;
        this.usuario = usuario;
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.recorrente = recorrente;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public DayOfWeek getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(DayOfWeek diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public boolean isRecorrente() {
        return recorrente;
    }

    public void setRecorrente(boolean recorrente) {
        this.recorrente = recorrente;
    }
}
