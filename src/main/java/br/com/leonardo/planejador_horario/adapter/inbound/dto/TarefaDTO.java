package br.com.leonardo.planejador_horario.adapter.inbound.dto;

import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados da tarefa")
public class TarefaDTO {

    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    @Schema(description = "Título da tarefa", example = "Implementar autenticação")
    private String titulo;

    @Schema(description = "Descrição detalhada da tarefa", example = "Implementar autenticação JWT com Spring Security")
    private String descricao;

    @NotNull(message = "O prazo é obrigatório")
    @Future(message = "O prazo deve ser uma data futura")
    @Schema(description = "Prazo para conclusão da tarefa", example = "2024-12-31")
    private LocalDate prazo;

    @NotNull(message = "A prioridade é obrigatória")
    @Schema(description = "Prioridade da tarefa", example = "ALTA")
    private Prioridade prioridade;

    @NotBlank(message = "A categoria é obrigatória")
    @Schema(description = "Categoria da tarefa", example = "Trabalho")
    private String categoria;

    @NotNull(message = "O tempo estimado é obrigatório")
    @Min(value = 1, message = "O tempo estimado deve ser maior que zero")
    @Schema(description = "Tempo estimado em minutos", example = "120")
    private Integer tempoEstimado;

    @NotNull(message = "O status é obrigatório")
    @Schema(description = "Status da tarefa", example = "PENDENTE")
    private Status status;

    private Long usuarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @Schema(description = "Data de início da tarefa", example = "2024-01-01")
    private LocalDate dataInicio;

    @Schema(description = "Data de fim da tarefa", example = "2024-01-31")
    private LocalDate dataFim;

    // Getters e Setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
} 