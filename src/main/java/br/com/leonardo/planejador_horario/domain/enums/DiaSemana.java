package br.com.leonardo.planejador_horario.domain.enums;

import java.time.DayOfWeek;

public enum DiaSemana {
    SEGUNDA_FEIRA("Segunda-feira", DayOfWeek.MONDAY),
    TERCA_FEIRA("Terça-feira", DayOfWeek.TUESDAY),
    QUARTA_FEIRA("Quarta-feira", DayOfWeek.WEDNESDAY),
    QUINTA_FEIRA("Quinta-feira", DayOfWeek.THURSDAY),
    SEXTA_FEIRA("Sexta-feira", DayOfWeek.FRIDAY),
    SABADO("Sábado", DayOfWeek.SATURDAY),
    DOMINGO("Domingo", DayOfWeek.SUNDAY);

    private final String descricao;
    private final DayOfWeek dayOfWeek;

    DiaSemana(String descricao, DayOfWeek dayOfWeek) {
        this.descricao = descricao;
        this.dayOfWeek = dayOfWeek;
    }

    public String getDescricao() {
        return descricao;
    }

    public DayOfWeek toDayOfWeek() {
        return dayOfWeek;
    }

    public static DiaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        for (DiaSemana dia : values()) {
            if (dia.dayOfWeek == dayOfWeek) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Dia da semana inválido: " + dayOfWeek);
    }
} 