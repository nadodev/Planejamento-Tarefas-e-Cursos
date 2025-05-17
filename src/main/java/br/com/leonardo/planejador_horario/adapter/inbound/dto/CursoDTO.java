package br.com.leonardo.planejador_horario.adapter.inbound.dto;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Representação de um curso no sistema")
public class CursoDTO {

    @Schema(description = "ID do curso", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome do curso", example = "Curso de Java Spring Boot", required = true)
    private String nome;

    @Schema(description = "Carga horária do curso em horas", example = "40", minimum = "1")
    private int cargaHoraria;

    @Schema(description = "Nível de prioridade do curso (1-5)", example = "3", minimum = "1", maximum = "5")
    private int prioridade;

    @Schema(description = "Data limite para conclusão do curso", example = "2024-12-31")
    private LocalDate prazoFinal;

    @Schema(description = "ID do usuário responsável pelo curso", example = "1", required = true)
    private Long usuario;

    public static CursoDTO fromEntity(CursoEntity entity) {
        CursoDTO dto = new CursoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCargaHoraria(entity.getCargaHoraria());
        dto.setPrioridade(entity.getPrioridade());
        dto.setPrazoFinal(entity.getPrazoFinal());
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

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getPrazoFinal() {
        return prazoFinal;
    }

    public void setPrazoFinal(LocalDate prazoFinal) {
        this.prazoFinal = prazoFinal;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
