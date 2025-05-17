package br.com.leonardo.planejador_horario.adapter.outbound.mapper;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.model.Compromisso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CompromissoMapper {

    public CompromissoEntity toEntity(Compromisso domain, UsuarioEntity usuario) {
        CompromissoEntity entity = new CompromissoEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setDiaDaSemana(domain.getDiaDaSemana());
        entity.setHorarioInicio(domain.getHorarioInicio());
        entity.setHorarioFim(domain.getHorarioFim());
        entity.setRecorrente(domain.isRecorrente());
        entity.setUsuario(usuario);
        return entity;
    }

    public Compromisso toDomain(CompromissoEntity entity) {
        return new Compromisso(
            entity.getId(),
            entity.getTitulo(),
            new Usuario(
                entity.getUsuario().getId(),
                entity.getUsuario().getNome(),
                entity.getUsuario().getEmail(),
                entity.getUsuario().getSenhaHash()
            ),
            entity.getDiaDaSemana(),
            entity.getHorarioInicio(),
            entity.getHorarioFim(),
            entity.isRecorrente()
        );
    }

    public CompromissoDTO toDTO(Compromisso domain) {
        return new CompromissoDTO(
            domain.getId(),
            domain.getTitulo(),
            domain.getDiaDaSemana(),
            domain.getHorarioInicio(),
            domain.getHorarioFim(),
            domain.isRecorrente(),
            domain.getUsuario().getId()
        );
    }

    public Compromisso toDomain(CompromissoDTO dto, Usuario usuario) {
        return new Compromisso(
            dto.getId(),
            dto.getTitulo(),
            usuario,
            dto.getDiaDaSemana(),
            dto.getHorarioInicio(),
            dto.getHorarioFim(),
            dto.isRecorrente()
        );
    }
} 