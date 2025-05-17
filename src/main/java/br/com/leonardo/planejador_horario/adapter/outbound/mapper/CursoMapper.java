package br.com.leonardo.planejador_horario.adapter.outbound.mapper;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.model.Curso;

public class CursoMapper {

    public static CursoEntity toEntity(Curso curso, UsuarioEntity usuario) {
        CursoEntity entity = new CursoEntity();
        entity.setId(curso.getId());
        entity.setNome(curso.getNome());
        entity.setDescricao(curso.getDescricao());
        entity.setCargaHoraria(curso.getCargaHoraria());
        entity.setPrioridade(curso.getPrioridade());
        entity.setPrazoFinal(curso.getPrazoFinal());
        entity.setUsuario(usuario);
        entity.setDataCriacao(curso.getDataCriacao());
        entity.setDataAtualizacao(curso.getDataAtualizacao());
        return entity;
    }

    public static Curso toDomain(CursoEntity entity) {
        Curso curso = new Curso();
        curso.setId(entity.getId());
        curso.setNome(entity.getNome());
        curso.setDescricao(entity.getDescricao());
        curso.setCargaHoraria(entity.getCargaHoraria());
        curso.setPrioridade(entity.getPrioridade());
        curso.setPrazoFinal(entity.getPrazoFinal());
        curso.setUsuarioId(entity.getUsuario().getId());
        curso.setDataCriacao(entity.getDataCriacao());
        curso.setDataAtualizacao(entity.getDataAtualizacao());
        return curso;
    }
}