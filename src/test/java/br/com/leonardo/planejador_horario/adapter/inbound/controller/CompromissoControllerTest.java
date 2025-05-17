package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.AtualizarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.CriarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.DeletarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.ListarCompromissoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompromissoControllerTest {

    @Mock
    private CriarCompromissoUseCase criarCompromissoUseCase;

    @Mock
    private ListarCompromissoUseCase listarCompromissoUseCase;

    @Mock
    private AtualizarCompromissoUseCase atualizarCompromissoUseCase;

    @Mock
    private DeletarCompromissoUseCase deletarCompromissoUseCase;

    private CompromissoController controller;
    private CompromissoDTO compromissoDTO;

    @BeforeEach
    void setUp() {
        controller = new CompromissoController(
            criarCompromissoUseCase,
            listarCompromissoUseCase,
            atualizarCompromissoUseCase,
            deletarCompromissoUseCase
        );

        compromissoDTO = new CompromissoDTO(
            1L,
            "Reunião",
            DayOfWeek.MONDAY,
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            true,
            1L
        );
    }

    @Test
    void deveCriarCompromissoComSucesso() {
        // Arrange
        when(criarCompromissoUseCase.criar(any(CompromissoDTO.class))).thenReturn(compromissoDTO);

        // Act
        ResponseEntity<?> response = controller.criar(compromissoDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(compromissoDTO, response.getBody());
        verify(criarCompromissoUseCase).criar(compromissoDTO);
    }

    @Test
    void deveRetornarErro404AoCriarQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(criarCompromissoUseCase.criar(any(CompromissoDTO.class)))
            .thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));

        // Act
        ResponseEntity<?> response = controller.criar(compromissoDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>)response.getBody()).containsKey("error"));
        verify(criarCompromissoUseCase).criar(compromissoDTO);
    }

    @Test
    void deveListarTodosCompromissosComSucesso() {
        // Arrange
        List<CompromissoDTO> compromissos = Arrays.asList(compromissoDTO);
        when(listarCompromissoUseCase.listarTodos()).thenReturn(compromissos);

        // Act
        ResponseEntity<List<CompromissoDTO>> response = controller.listar(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(compromissos, response.getBody());
        verify(listarCompromissoUseCase).listarTodos();
        verify(listarCompromissoUseCase, never()).listarPorUsuario(any());
    }

    @Test
    void deveListarCompromissosPorUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;
        List<CompromissoDTO> compromissos = Arrays.asList(compromissoDTO);
        when(listarCompromissoUseCase.listarPorUsuario(usuarioId)).thenReturn(compromissos);

        // Act
        ResponseEntity<List<CompromissoDTO>> response = controller.listar(usuarioId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(compromissos, response.getBody());
        verify(listarCompromissoUseCase).listarPorUsuario(usuarioId);
        verify(listarCompromissoUseCase, never()).listarTodos();
    }

    @Test
    void deveBuscarCompromissoPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        when(listarCompromissoUseCase.buscarPorId(id)).thenReturn(compromissoDTO);

        // Act
        ResponseEntity<?> response = controller.buscarPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(compromissoDTO, response.getBody());
        verify(listarCompromissoUseCase).buscarPorId(id);
    }

    @Test
    void deveRetornarErro404AoBuscarQuandoCompromissoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        when(listarCompromissoUseCase.buscarPorId(id))
            .thenThrow(new CompromissoNaoEncontradoException("Compromisso não encontrado"));

        // Act
        ResponseEntity<?> response = controller.buscarPorId(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>)response.getBody()).containsKey("error"));
        verify(listarCompromissoUseCase).buscarPorId(id);
    }

    @Test
    void deveAtualizarCompromissoComSucesso() {
        // Arrange
        Long id = 1L;
        when(atualizarCompromissoUseCase.atualizar(eq(id), any(CompromissoDTO.class)))
            .thenReturn(compromissoDTO);

        // Act
        ResponseEntity<?> response = controller.atualizar(id, compromissoDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(compromissoDTO, response.getBody());
        verify(atualizarCompromissoUseCase).atualizar(id, compromissoDTO);
    }

    @Test
    void deveDeletarCompromissoComSucesso() {
        // Arrange
        Long id = 1L;
        doNothing().when(deletarCompromissoUseCase).deletar(id);

        // Act
        ResponseEntity<?> response = controller.deletar(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deletarCompromissoUseCase).deletar(id);
    }

    @Test
    void deveRetornarErro404AoDeletarQuandoCompromissoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        doThrow(new CompromissoNaoEncontradoException("Compromisso não encontrado"))
            .when(deletarCompromissoUseCase).deletar(id);

        // Act
        ResponseEntity<?> response = controller.deletar(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>)response.getBody()).containsKey("error"));
        verify(deletarCompromissoUseCase).deletar(id);
    }
} 