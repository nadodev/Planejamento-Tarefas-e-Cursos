package br.com.leonardo.planejador_horario.domain.validator;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class CursoValidator {

    public void validar(@Valid CursoDTO curso) {
        if (curso == null) {
            throw new CursoException("Curso não pode ser nulo");
        }

        if (curso.getNome() == null || curso.getNome().trim().isEmpty()) {
            throw new CursoException("Nome do curso é obrigatório");
        }

        if (curso.getUsuarioId() == null) {
            throw new CursoException("ID do usuário é obrigatório");
        }
    }
}