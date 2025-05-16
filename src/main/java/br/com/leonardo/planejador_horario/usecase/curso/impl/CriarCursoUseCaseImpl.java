package br.com.leonardo.planejador_horario.usecase.curso.impl;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.JpaCursoRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.JpaUsuarioRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.mapper.CursoMapper;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.exception.CursoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.model.Curso;
import br.com.leonardo.planejador_horario.domain.model.Usuario;
import br.com.leonardo.planejador_horario.domain.validator.CursoValidator;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CriarCursoUseCaseImpl implements CriarCursoUseCase {

    private final JpaCursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final CursoValidator cursoValidator;

    private final JpaUsuarioRepository usuarioRepository;


    public CriarCursoUseCaseImpl(JpaCursoRepository cursoRepository,
                                 CursoMapper cursoMapper, CursoValidator cursoValidator, JpaUsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.cursoValidator = cursoValidator;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Curso criar(@Valid CursoDTO cursoDTO) {
        cursoValidator.validar(cursoDTO);

        Optional<UsuarioEntity> usuario = usuarioRepository.findById(cursoDTO.getUsuario());

        try {
            Curso curso = new Curso();
            curso.setNome(cursoDTO.getNome());
            curso.setCargaHoraria(cursoDTO.getCargaHoraria());
            curso.setPrioridade(cursoDTO.getPrioridade());
            curso.setPrazoFinal(cursoDTO.getPrazoFinal());
            usuario.ifPresent(curso::setUsuario);

            CursoEntity entity = cursoMapper.toEntity(curso);
            CursoEntity savedEntity = cursoRepository.save(entity);

            return cursoMapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            throw new CursoException("Erro ao salvar curso: " + e.getMessage());
        }
    }
}