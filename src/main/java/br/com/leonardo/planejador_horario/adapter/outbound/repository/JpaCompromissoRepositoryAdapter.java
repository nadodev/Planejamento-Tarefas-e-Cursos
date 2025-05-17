package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.CompromissoEntity;
import br.com.leonardo.planejador_horario.application.port.out.CompromissoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCompromissoRepositoryAdapter implements CompromissoRepository {

    private final JpaCompromissoRepository jpaCompromissoRepository;

    public JpaCompromissoRepositoryAdapter(JpaCompromissoRepository jpaCompromissoRepository) {
        this.jpaCompromissoRepository = jpaCompromissoRepository;
    }

    @Override
    public CompromissoEntity save(CompromissoEntity compromisso) {
        return jpaCompromissoRepository.save(compromisso);
    }

    @Override
    public Optional<CompromissoEntity> findById(Long id) {
        return jpaCompromissoRepository.findById(id);
    }

    @Override
    public List<CompromissoEntity> findAll() {
        return jpaCompromissoRepository.findAll();
    }

    @Override
    public List<CompromissoEntity> findByUsuarioId(Long usuarioId) {
        return jpaCompromissoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public void delete(CompromissoEntity compromisso) {
        jpaCompromissoRepository.delete(compromisso);
    }

    @Override
    public void deleteById(Long id) {
        jpaCompromissoRepository.deleteById(id);
    }
} 