package br.com.leonardo.planejador_horario.usecase.curso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CursoMapper;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoRepository;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import br.com.leonardo.planejador_horario.application.port.out.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CriarCursoUseCaseImpl implements CriarCursoUseCase {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CriarCursoUseCaseImpl(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public CursoDTO criar(CursoDTO cursoDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(cursoDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Curso curso = new Curso();
        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        curso.setUsuarioId(usuario.getId());
        curso.setDataCriacao(LocalDateTime.now());

        var cursoEntity = CursoMapper.toEntity(curso, usuario);
        var cursoSalvo = cursoRepository.save(cursoEntity);

        return CursoDTO.fromEntity(cursoSalvo);
    }
}