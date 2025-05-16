package br.com.leonardo.planejador_horario.adapter.inbound.dto;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;

import java.time.LocalDate;

public class CursoDTO {

    private Long id;
    private String nome;
    private int cargaHoraria;
    private int prioridade;
    private LocalDate prazoFinal;

    public static CursoDTO fromEntity(CursoEntity entity) {
        CursoDTO dto = new CursoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCargaHoraria(entity.getCargaHoraria());
        dto.setPrioridade(entity.getPrioridade());
        return dto;
    }

    private Long usuario;


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
