package br.com.leonardo.planejador_horario.usecase.curso.impl;

import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaCursoRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaUsuarioRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.UsuarioMapper;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import br.com.leonardo.planejador_horario.usecase.curso.ListarCursosUseCase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListarCursosUseCaseImpl implements ListarCursosUseCase {

    private final JpaCursoRepository cursoRepository;

    private final JpaUsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public ListarCursosUseCaseImpl(JpaCursoRepository cursoRepository, JpaUsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }


    @Override
    public Optional<Usuario> listarPorUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuarioMapper::toDomain);
    }
}