package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.CursoEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.curso.CriarCursoUseCase;
import br.com.leonardo.planejador_horario.usecase.curso.DeletaCursoUseCase;
import br.com.leonardo.planejador_horario.usecase.curso.ListarCursosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CriarCursoUseCase criarCursoUseCase;

    @Mock
    private ListarCursosUseCase listarCursosUseCase;

    @Mock
    private DeletaCursoUseCase deletarCursoUseCase;

    @InjectMocks
    private CursoController cursoController;

    private CursoDTO cursoDTO;
    private CursoDTO cursoDTOResponse;
    private CursoEntity cursoEntity;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("Usuário Teste");
        usuarioEntity.setEmail("usuario@teste.com");
        usuarioEntity.setSenhaHash("hash");

        cursoDTO = new CursoDTO();
        cursoDTO.setNome("Java Spring Boot");
        cursoDTO.setDescricao("Curso completo de Spring Boot");
        cursoDTO.setCargaHoraria(40);
        cursoDTO.setPrioridade(3);
        cursoDTO.setPrazoFinal(LocalDate.now().plusMonths(1));
        cursoDTO.setUsuarioId(1L);

        cursoDTOResponse = new CursoDTO();
        cursoDTOResponse.setId(1L);
        cursoDTOResponse.setNome("Java Spring Boot");
        cursoDTOResponse.setDescricao("Curso completo de Spring Boot");
        cursoDTOResponse.setCargaHoraria(40);
        cursoDTOResponse.setPrioridade(3);
        cursoDTOResponse.setPrazoFinal(LocalDate.now().plusMonths(1));
        cursoDTOResponse.setUsuarioId(1L);
        cursoDTOResponse.setDataCriacao(LocalDateTime.now());

        cursoEntity = new CursoEntity();
        cursoEntity.setId(1L);
        cursoEntity.setNome("Java Spring Boot");
        cursoEntity.setDescricao("Curso completo de Spring Boot");
        cursoEntity.setCargaHoraria(40);
        cursoEntity.setPrioridade(3);
        cursoEntity.setPrazoFinal(LocalDate.now().plusMonths(1));
        cursoEntity.setUsuario(usuarioEntity);
        cursoEntity.setDataCriacao(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar um curso com sucesso")
    void deveCriarCursoComSucesso() {
        // Arrange
        when(criarCursoUseCase.criar(any(CursoDTO.class))).thenReturn(cursoDTOResponse);

        // Act
        ResponseEntity<?> response = cursoController.criarCurso(cursoDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(criarCursoUseCase, times(1)).criar(any(CursoDTO.class));
    }

    @Test
    @DisplayName("Deve retornar erro quando usuário não encontrado ao criar curso")
    void deveRetornarErroQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(criarCursoUseCase.criar(any(CursoDTO.class)))
                .thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado com ID: 1"));

        // Act
        ResponseEntity<?> response = cursoController.criarCurso(cursoDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(criarCursoUseCase, times(1)).criar(any(CursoDTO.class));
    }

    @Test
    @DisplayName("Deve listar todos os cursos com sucesso")
    void deveListarTodosCursosComSucesso() {
        // Arrange
        when(listarCursosUseCase.listarCurso()).thenReturn(Arrays.asList(cursoEntity));

        // Act
        ResponseEntity<List<CursoDTO>> response = cursoController.listarCursos();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        verify(listarCursosUseCase, times(1)).listarCurso();
    }

    @Test
    @DisplayName("Deve listar cursos por usuário com sucesso")
    void deveListarCursosPorUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;
        when(listarCursosUseCase.listarPorUsuario(usuarioId)).thenReturn(Arrays.asList(cursoEntity));

        // Act
        ResponseEntity<List<CursoDTO>> response = cursoController.listarCursos(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        verify(listarCursosUseCase, times(1)).listarPorUsuario(usuarioId);
    }

    @Test
    @DisplayName("Deve deletar curso com sucesso")
    void deveDeletarCursoComSucesso() {
        // Arrange
        Long cursoId = 1L;
        doNothing().when(deletarCursoUseCase).deletar(cursoId);

        // Act
        ResponseEntity<Void> response = cursoController.deletar(cursoId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deletarCursoUseCase, times(1)).deletar(cursoId);
    }
} 