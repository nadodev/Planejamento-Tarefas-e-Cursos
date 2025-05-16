package br.com.leonardo.planejador_horario.adapter.outbound;

import br.com.leonardo.planejador_horario.adapter.outbound.repository.CursoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.repository.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
