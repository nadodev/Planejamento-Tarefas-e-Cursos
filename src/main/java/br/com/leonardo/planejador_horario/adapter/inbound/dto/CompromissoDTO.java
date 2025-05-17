package br.com.leonardo.planejador_horario.adapter.inbound.dto;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Dados do compromisso")
public class CompromissoDTO {

    @Schema(description = "ID do compromisso", example = "1")
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    @Schema(description = "Título do compromisso", example = "Reunião de Equipe")
    private String titulo;

    @NotNull(message = "O dia da semana é obrigatório")
    @Schema(
        description = "Dia da semana do compromisso (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)", 
        example = "MONDAY"
    )
    private DayOfWeek diaDaSemana;

    @NotNull(message = "Horário de início é obrigatório")
    @Schema(description = "Horário de início do compromisso (HH:mm)", example = "14:30")
    private LocalTime horarioInicio;

    @NotNull(message = "Horário fim é obrigatório")
    @Schema(description = "Horário de término do compromisso (HH:mm)", example = "15:30")
    private LocalTime horarioFim;

    @Schema(
        description = "Indica se o compromisso é recorrente (se repete toda semana)", 
        example = "true",
        defaultValue = "false"
    )
    private boolean recorrente;

    @Schema(description = "ID do usuário responsável pelo compromisso", example = "1")
    private Long usuarioId;

    // Construtor padrão
    public CompromissoDTO() {
    }

    // Construtor com todos os campos
    public CompromissoDTO(Long id, String titulo, DayOfWeek diaDaSemana, LocalTime horarioInicio, LocalTime horarioFim, boolean recorrente, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.recorrente = recorrente;
        this.usuarioId = usuarioId;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
