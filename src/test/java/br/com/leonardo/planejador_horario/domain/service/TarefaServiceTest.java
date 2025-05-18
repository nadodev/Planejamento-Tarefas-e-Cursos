package br.com.leonardo.planejador_horario.domain.service;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.TarefaDTO;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.exception.TarefaNaoEncontradaException;
import br.com.leonardo.planejador_horario.domain.repository.TarefaRepository;
import br.com.leonardo.planejador_horario.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Tarefa tarefa;
    private TarefaDTO tarefaDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");

        tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Tarefa Teste");
        tarefa.setDescricao("Descrição Teste");
        tarefa.setStatus(Status.PENDENTE);
        tarefa.setPrioridade(Prioridade.MEDIA);
        tarefa.setCategoria("TESTE");
        tarefa.setDataInicio(LocalDate.now());
        tarefa.setDataFim(LocalDate.now().plusDays(1));
        tarefa.setUsuario(usuario);

        tarefaDTO = new TarefaDTO();
        tarefaDTO.setTitulo("Tarefa Teste");
        tarefaDTO.setDescricao("Descrição Teste");
        tarefaDTO.setStatus(Status.PENDENTE);
        tarefaDTO.setPrioridade(Prioridade.MEDIA);
        tarefaDTO.setCategoria("TESTE");
        tarefaDTO.setDataInicio(LocalDate.now());
        tarefaDTO.setDataFim(LocalDate.now().plusDays(1));
    }

    @Test
    void criarTarefa_DeveRetornarTarefaCriada() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        TarefaDTO result = tarefaService.criarTarefa(tarefaDTO);

        assertNotNull(result);
        assertEquals(tarefaDTO.getTitulo(), result.getTitulo());
        verify(tarefaRepository).save(any(Tarefa.class));
    }

    @Test
    void listarTarefas_DeveRetornarListaPaginada() {
        Page<Tarefa> page = new PageImpl<>(Arrays.asList(tarefa));
        when(tarefaRepository.findByUsuarioId(anyLong(), any(PageRequest.class))).thenReturn(page);

        Page<TarefaDTO> result = tarefaService.listarTarefas(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(tarefaRepository).findByUsuarioId(anyLong(), any(PageRequest.class));
    }

    @Test
    void buscarTarefaPorId_DeveRetornarTarefa() {
        when(tarefaRepository.findById(anyLong())).thenReturn(Optional.of(tarefa));

        TarefaDTO result = tarefaService.buscarTarefaPorId(1L);

        assertNotNull(result);
        assertEquals(tarefa.getId(), result.getId());
        verify(tarefaRepository).findById(1L);
    }

    @Test
    void buscarTarefaPorId_Inexistente_DeveLancarExcecao() {
        when(tarefaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> tarefaService.buscarTarefaPorId(1L));
    }

    @Test
    void atualizarTarefa_DeveRetornarTarefaAtualizada() {
        when(tarefaRepository.findById(anyLong())).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        tarefaDTO.setTitulo("Título Atualizado");
        TarefaDTO result = tarefaService.atualizarTarefa(1L, tarefaDTO);

        assertNotNull(result);
        assertEquals("Título Atualizado", result.getTitulo());
        verify(tarefaRepository).save(any(Tarefa.class));
    }

    @Test
    void excluirTarefa_DeveExcluirTarefa() {
        when(tarefaRepository.findById(anyLong())).thenReturn(Optional.of(tarefa));
        doNothing().when(tarefaRepository).delete(any(Tarefa.class));

        tarefaService.excluirTarefa(1L);

        verify(tarefaRepository).delete(tarefa);
    }

    @Test
    void buscarTarefasPorStatus_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndStatus(anyLong(), any(Status.class)))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasPorStatus(1L, Status.PENDENTE);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndStatus(1L, Status.PENDENTE);
    }

    @Test
    void buscarTarefasPorPrioridade_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndPrioridade(anyLong(), any(Prioridade.class)))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasPorPrioridade(1L, Prioridade.MEDIA);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndPrioridade(1L, Prioridade.MEDIA);
    }

    @Test
    void buscarTarefasPorCategoria_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndCategoria(anyLong(), anyString()))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasPorCategoria(1L, "TESTE");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndCategoria(1L, "TESTE");
    }

    @Test
    void buscarTarefasPorPeriodo_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndDataInicioBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasPorPeriodo(
            1L, LocalDate.now(), LocalDate.now().plusDays(1));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndDataInicioBetween(
            anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void buscarTarefasAtrasadas_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndDataFimBeforeAndStatusNot(
            anyLong(), any(LocalDate.class), any(Status.class)))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasAtrasadas(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndDataFimBeforeAndStatusNot(
            anyLong(), any(LocalDate.class), any(Status.class));
    }

    @Test
    void buscarTarefasParaHoje_DeveRetornarLista() {
        when(tarefaRepository.findByUsuarioIdAndDataInicio(anyLong(), any(LocalDate.class)))
                .thenReturn(Arrays.asList(tarefa));

        List<TarefaDTO> result = tarefaService.buscarTarefasParaHoje(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tarefaRepository).findByUsuarioIdAndDataInicio(anyLong(), any(LocalDate.class));
    }
} 