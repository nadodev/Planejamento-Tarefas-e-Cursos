package br.com.leonardo.planejador_horario.domain.repository;

import br.com.leonardo.planejador_horario.domain.entities.Tarefa;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TarefaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("senha123");
        usuario = usuarioRepository.save(usuario);

        tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa Teste");
        tarefa.setDescricao("Descrição Teste");
        tarefa.setStatus(Status.PENDENTE);
        tarefa.setPrioridade(Prioridade.MEDIA);
        tarefa.setCategoria("TESTE");
        tarefa.setDataInicio(LocalDate.now());
        tarefa.setDataFim(LocalDate.now().plusDays(1));
        tarefa.setUsuario(usuario);
        tarefa = tarefaRepository.save(tarefa);
    }

    @Test
    void findByUsuarioId_DeveRetornarListaPaginada() {
        Page<Tarefa> result = tarefaRepository.findByUsuarioId(usuario.getId(), PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo(tarefa.getTitulo());
    }

    @Test
    void findByUsuarioIdAndStatus_DeveRetornarLista() {
        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndStatus(usuario.getId(), Status.PENDENTE);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Status.PENDENTE);
    }

    @Test
    void findByUsuarioIdAndPrioridade_DeveRetornarLista() {
        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndPrioridade(usuario.getId(), Prioridade.MEDIA);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPrioridade()).isEqualTo(Prioridade.MEDIA);
    }

    @Test
    void findByUsuarioIdAndCategoria_DeveRetornarLista() {
        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndCategoria(usuario.getId(), "TESTE");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategoria()).isEqualTo("TESTE");
    }

    @Test
    void findByUsuarioIdAndDataInicioBetween_DeveRetornarLista() {
        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndDataInicioBetween(
            usuario.getId(), LocalDate.now(), LocalDate.now().plusDays(1));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDataInicio()).isEqualTo(LocalDate.now());
    }

    @Test
    void findByUsuarioIdAndDataFimBeforeAndStatusNot_DeveRetornarLista() {
        // Criar uma tarefa atrasada
        Tarefa tarefaAtrasada = new Tarefa();
        tarefaAtrasada.setTitulo("Tarefa Atrasada");
        tarefaAtrasada.setDescricao("Descrição Teste");
        tarefaAtrasada.setStatus(Status.PENDENTE);
        tarefaAtrasada.setPrioridade(Prioridade.MEDIA);
        tarefaAtrasada.setCategoria("TESTE");
        tarefaAtrasada.setDataInicio(LocalDate.now().minusDays(2));
        tarefaAtrasada.setDataFim(LocalDate.now().minusDays(1));
        tarefaAtrasada.setUsuario(usuario);
        tarefaRepository.save(tarefaAtrasada);

        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndDataFimBeforeAndStatusNot(
            usuario.getId(), LocalDate.now(), Status.CONCLUIDA);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitulo()).isEqualTo("Tarefa Atrasada");
    }

    @Test
    void findByUsuarioIdAndDataInicio_DeveRetornarLista() {
        List<Tarefa> result = tarefaRepository.findByUsuarioIdAndDataInicio(usuario.getId(), LocalDate.now());

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDataInicio()).isEqualTo(LocalDate.now());
    }
} 