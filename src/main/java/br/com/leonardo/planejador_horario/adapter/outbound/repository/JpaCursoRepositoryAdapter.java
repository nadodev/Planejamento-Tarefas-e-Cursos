package br.com.leonardo.planejador_horario.adapter.outbound.repository;


import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaCursoRepository;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.application.port.out.CursoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaCursoRepositoryAdapter implements CursoRepository {

    private final JpaCursoRepository jpaCursoRepository;

    public JpaCursoRepositoryAdapter(JpaCursoRepository jpaCursoRepository) {
        this.jpaCursoRepository = jpaCursoRepository;
    }

    @Override
    public Optional<CursoEntity> findById(Long id) {
        return jpaCursoRepository.findById(id);
    }

    @Override
    public void delete(CursoEntity curso) {
        jpaCursoRepository.delete(curso);
    }
}
