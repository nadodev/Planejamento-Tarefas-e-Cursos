package br.com.leonardo.planejador_horario.adapter.inbound.dto;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.domain.entities.Curso.NivelCurso;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "Dados do curso")
public class CursoDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome do curso", example = "Java Completo")
    private String nome;

    @Schema(description = "Descrição do curso", example = "Curso completo de Java")
    private String descricao;

    @NotNull(message = "A carga horária é obrigatória")
    @Min(value = 1, message = "A carga horária deve ser maior que zero")
    @Schema(description = "Carga horária do curso em horas", example = "40")
    private Integer cargaHoraria;

    @NotNull(message = "A prioridade é obrigatória")
    @Min(value = 1, message = "A prioridade deve ser maior que zero")
    @Max(value = 5, message = "A prioridade deve ser menor ou igual a 5")
    @Schema(description = "Prioridade do curso (1-5)", example = "3")
    private Integer prioridade;

    @NotNull(message = "O prazo final é obrigatório")
    @Future(message = "O prazo final deve ser uma data futura")
    @Schema(description = "Prazo final para conclusão do curso", example = "2024-12-31")
    private LocalDate prazoFinal;

    @NotBlank(message = "A plataforma é obrigatória")
    @Schema(description = "Plataforma do curso", example = "Alura")
    private String plataforma;

    @Schema(description = "Link do curso (opcional)", example = "https://www.alura.com.br/curso-java")
    private String link;

    @NotNull(message = "O nível é obrigatório")
    @Schema(description = "Nível do curso", example = "INTERMEDIARIO")
    private NivelCurso nivel;

    private Long usuarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static CursoDTO fromEntity(CursoEntity entity) {
        CursoDTO dto = new CursoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setCargaHoraria(entity.getCargaHoraria());
        dto.setPrioridade(entity.getPrioridade());
        dto.setPrazoFinal(entity.getPrazoFinal());
        dto.setPlataforma(entity.getPlataforma());
        dto.setLink(entity.getLink());
        dto.setNivel(entity.getNivel());
        dto.setUsuarioId(entity.getUsuario().getId());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setDataAtualizacao(entity.getDataAtualizacao());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getPrazoFinal() {
        return prazoFinal;
    }

    public void setPrazoFinal(LocalDate prazoFinal) {
        this.prazoFinal = prazoFinal;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public NivelCurso getNivel() {
        return nivel;
    }

    public void setNivel(NivelCurso nivel) {
        this.nivel = nivel;
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
}
