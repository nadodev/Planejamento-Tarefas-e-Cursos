package br.com.leonardo.planejador_horario.domain.validator;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.CursoNomeObrigatorioException;
import br.com.leonardo.planejador_horario.domain.exception.CursoResponsavelObrigatorioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class CursoValidator {

    public void validar(@Valid CursoDTO curso) {
        if (curso == null) {
            throw new CursoException("Curso não pode ser nulo");
        }

        if (curso.getNome() == null || curso.getNome().isBlank()) {
            throw new CursoNomeObrigatorioException();
        }

        if (curso.getUsuario() == null) {
            throw new CursoResponsavelObrigatorioException();
        }

    }
}