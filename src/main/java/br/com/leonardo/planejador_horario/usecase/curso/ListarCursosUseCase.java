package br.com.leonardo.planejador_horario.usecase.curso;


import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListarCursosUseCase {
    List<CursoEntity> listarPorUsuario(Long usuarioId);
    List<CursoEntity> listarCurso();
}