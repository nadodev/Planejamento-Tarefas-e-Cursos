package br.com.leonardo.planejador_horario.usecase.curso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaCursoRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaUsuarioRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CursoMapper;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.application.port.out.CursoRepository;
import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import br.com.leonardo.planejador_horario.domain.validator.CursoValidator;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


@Service
public class CriarCursoUseCaseImpl implements CriarCursoUseCase {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final CursoValidator cursoValidator;
    private final JpaUsuarioRepository usuarioRepository;

    public CriarCursoUseCaseImpl(CursoRepository cursoRepository,
                                 CursoMapper cursoMapper, CursoValidator cursoValidator, JpaUsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.cursoValidator = cursoValidator;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Curso criar(@Valid CursoDTO cursoDTO) {
        cursoValidator.validar(cursoDTO);

        UsuarioEntity usuario = usuarioRepository.findById(cursoDTO.getUsuario())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(Arrays.asList(cursoDTO.getUsuario())));

        try {
            Curso curso = new Curso();
            curso.setNome(cursoDTO.getNome());
            curso.setCargaHoraria(cursoDTO.getCargaHoraria());
            curso.setPrioridade(cursoDTO.getPrioridade());
            curso.setPrazoFinal(cursoDTO.getPrazoFinal());
            curso.setUsuario(usuario);

            CursoEntity entity = cursoMapper.toEntity(curso);
            CursoEntity savedEntity = cursoRepository.save(entity);

            return cursoMapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            throw new CursoException("Erro ao salvar curso: " + e.getMessage());
        }
    }
}