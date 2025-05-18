package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.TarefaDTO;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TarefaControllerTest {

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    private TarefaDTO tarefaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tarefaDTO = new TarefaDTO();
        tarefaDTO.setId(1L);
        tarefaDTO.setTitulo("Teste");
        tarefaDTO.setDescricao("Descrição teste");
        tarefaDTO.setStatus(Status.PENDENTE);
        tarefaDTO.setPrioridade(Prioridade.MEDIA);
        tarefaDTO.setDataInicio(LocalDate.now());
        tarefaDTO.setDataFim(LocalDate.now().plusDays(1));
    }

    @Test
    void criarTarefa_DeveRetornarTarefaCriada() {
        when(tarefaService.criarTarefa(any(TarefaDTO.class))).thenReturn(tarefaDTO);

        ResponseEntity<TarefaDTO> response = tarefaController.criarTarefa(tarefaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tarefaDTO, response.getBody());
        verify(tarefaService).criarTarefa(tarefaDTO);
    }

    @Test
    void listarTarefas_DeveRetornarListaPaginada() {
        Page<TarefaDTO> page = new PageImpl<>(Arrays.asList(tarefaDTO));
        when(tarefaService.listarTarefas(anyLong(), any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<TarefaDTO>> response = tarefaController.listarTarefas(1L, PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(tarefaService).listarTarefas(1L, PageRequest.of(0, 10));
    }

    @Test
    void buscarTarefaPorId_DeveRetornarTarefa() {
        when(tarefaService.buscarTarefaPorId(anyLong())).thenReturn(tarefaDTO);

        ResponseEntity<TarefaDTO> response = tarefaController.buscarTarefaPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefaDTO, response.getBody());
        verify(tarefaService).buscarTarefaPorId(1L);
    }

    @Test
    void atualizarTarefa_DeveRetornarTarefaAtualizada() {
        when(tarefaService.atualizarTarefa(anyLong(), any(TarefaDTO.class))).thenReturn(tarefaDTO);

        ResponseEntity<TarefaDTO> response = tarefaController.atualizarTarefa(1L, tarefaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefaDTO, response.getBody());
        verify(tarefaService).atualizarTarefa(1L, tarefaDTO);
    }

    @Test
    void excluirTarefa_DeveRetornarNoContent() {
        doNothing().when(tarefaService).excluirTarefa(anyLong());

        tarefaController.excluirTarefa(1L);

        verify(tarefaService).excluirTarefa(1L);
    }

    @Test
    void buscarTarefasPorStatus_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasPorStatus(anyLong(), any(Status.class))).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasPorStatus(1L, Status.PENDENTE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasPorStatus(1L, Status.PENDENTE);
    }

    @Test
    void buscarTarefasPorPrioridade_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasPorPrioridade(anyLong(), any(Prioridade.class))).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasPorPrioridade(1L, Prioridade.MEDIA);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasPorPrioridade(1L, Prioridade.MEDIA);
    }

    @Test
    void buscarTarefasPorCategoria_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasPorCategoria(anyLong(), anyString())).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasPorCategoria(1L, "ESTUDOS");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasPorCategoria(1L, "ESTUDOS");
    }

    @Test
    void buscarTarefasPorPeriodo_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasPorPeriodo(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasPorPeriodo(
            1L, LocalDate.now(), LocalDate.now().plusDays(1));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasPorPeriodo(1L, LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    void buscarTarefasAtrasadas_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasAtrasadas(anyLong())).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasAtrasadas(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasAtrasadas(1L);
    }

    @Test
    void buscarTarefasParaHoje_DeveRetornarLista() {
        List<TarefaDTO> tarefas = Arrays.asList(tarefaDTO);
        when(tarefaService.buscarTarefasParaHoje(anyLong())).thenReturn(tarefas);

        ResponseEntity<List<TarefaDTO>> response = tarefaController.buscarTarefasParaHoje(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tarefas, response.getBody());
        verify(tarefaService).buscarTarefasParaHoje(1L);
    }
} 