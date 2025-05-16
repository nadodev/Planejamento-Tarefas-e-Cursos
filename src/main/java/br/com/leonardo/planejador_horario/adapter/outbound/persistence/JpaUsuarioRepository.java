package br.com.leonardo.planejador_horario.adapter.outbound.persistence;

import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
