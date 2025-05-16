package br.com.leonardo.planejador_horario.usecase.curso;


import br.com.leonardo.planejador_horario.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ListarCursosUseCase {
    Optional<Usuario> listarPorUsuario(Long usuarioId);
}