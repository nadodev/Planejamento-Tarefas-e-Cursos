package br.com.leonardo.planejador_horario.domain.repository;

import br.com.leonardo.planejador_horario.domain.entities.Disponibilidade;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DisponibilidadeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Disponibilidade disponibilidade;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        disponibilidade = new Disponibilidade();
        disponibilidade.setDiaSemana(DiaSemana.SEGUNDA);
        disponibilidade.setHoraInicio(LocalTime.of(9, 0));
        disponibilidade.setHoraFim(LocalTime.of(17, 0));
        disponibilidade.setUsuario(usuario);
        disponibilidade = disponibilidadeRepository.save(disponibilidade);
    }

    @Test
    void findByUsuarioId_DeveRetornarLista() {
        List<Disponibilidade> result = disponibilidadeRepository.findByUsuarioId(usuario.getId());

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDiaSemana()).isEqualTo(DiaSemana.SEGUNDA);
    }

    @Test
    void findByUsuarioIdAndDiaSemana_DeveRetornarLista() {
        List<Disponibilidade> result = disponibilidadeRepository.findByUsuarioIdAndDiaSemana(
            usuario.getId(), DiaSemana.SEGUNDA);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDiaSemana()).isEqualTo(DiaSemana.SEGUNDA);
    }

    @Test
    void findByUsuarioIdAndDiaSemana_DiaDiferente_DeveRetornarListaVazia() {
        List<Disponibilidade> result = disponibilidadeRepository.findByUsuarioIdAndDiaSemana(
            usuario.getId(), DiaSemana.TERCA);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void findByUsuarioId_UsuarioInexistente_DeveRetornarListaVazia() {
        List<Disponibilidade> result = disponibilidadeRepository.findByUsuarioId(999L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
} 