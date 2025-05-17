package br.com.leonardo.planejador_horario.adapter.outbound.repository;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaUsuarioRepository;
import br.com.leonardo.planejador_horario.application.port.out.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    public JpaUsuarioRepositoryAdapter(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return jpaUsuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return jpaUsuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioEntity> findById(Long id) {
        return jpaUsuarioRepository.findById(id);
    }

    @Override
    public Optional<UsuarioEntity> findByEmail(String email) {
        return jpaUsuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUsuarioRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaUsuarioRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUsuarioRepository.existsById(id);
    }
} 