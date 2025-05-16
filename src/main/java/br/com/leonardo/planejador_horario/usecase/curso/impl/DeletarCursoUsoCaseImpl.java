package br.com.leonardo.planejador_horario.usecase.curso.impl;

import br.com.leonardo.planejador_horario.adapter.outbound.JpaCursoRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoEntity;
import br.com.leonardo.planejador_horario.application.port.out.CursoRepository;
import br.com.leonardo.planejador_horario.domain.exception.CursoNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.curso.DeletaCursoUseCase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarCursoUsoCaseImpl  implements DeletaCursoUseCase {

    private final CursoRepository cursoRepository;

    public DeletarCursoUsoCaseImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public void deletar(Long id) {
        CursoEntity curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNaoEncontradoException(id));

        cursoRepository.delete(curso);
    }
}
