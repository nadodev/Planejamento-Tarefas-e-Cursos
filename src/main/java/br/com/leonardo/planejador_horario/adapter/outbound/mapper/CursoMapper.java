package br.com.leonardo.planejador_horario.adapter.outbound.mapper;

import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoEntity;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    private final UsuarioMapper usuarioMapper;

    public CursoMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public CursoEntity toEntity(Curso domain) {
        CursoEntity entity = new CursoEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCargaHoraria(domain.getCargaHoraria());
        entity.setPrioridade(domain.getPrioridade());
        entity.setPrazoFinal(domain.getPrazoFinal());

        if (domain.getUsuario() != null) {
            entity.setUsuario(usuarioMapper.toEntity(domain.getUsuario()));
        }
        return entity;
    }

    public Curso toDomain(CursoEntity entity) {
        return new Curso(
                entity.getId(),
                entity.getNome(),
                entity.getCargaHoraria(),
                entity.getPrioridade(),
                entity.getPrazoFinal(),
                entity.getUsuario()
        );
    }
}