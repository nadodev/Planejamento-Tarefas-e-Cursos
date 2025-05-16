package br.com.leonardo.planejador_horario.usecase.curso;


import br.com.leonardo.planejador_horario.domain.model.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface ListarCursosUseCase {
    Optional<Usuario> listarPorUsuario(Long usuarioId);
}