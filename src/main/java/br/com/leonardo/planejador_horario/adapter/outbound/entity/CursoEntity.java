package br.com.leonardo.planejador_horario.adapter.outbound.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cursos")
public class CursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int cargaHoraria;

    private int prioridade;

    private LocalDate prazoFinal;

    public CursoEntity() {
    }

    public CursoEntity(Long id, String nome, int cargaHoraria, int prioridade, LocalDate prazoFinal, UsuarioEntity usuario) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.prioridade = prioridade;
        this.prazoFinal = prazoFinal;
        this.usuario = usuario;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;


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

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

}

