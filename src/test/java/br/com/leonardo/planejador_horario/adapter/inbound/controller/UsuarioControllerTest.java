package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.domain.exception.EmailJaCadastradoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.usuario.AtualizarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.CriarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.DeletarUsuarioUseCase;
import br.com.leonardo.planejador_horario.usecase.usuario.ListarUsuariosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private CriarUsuarioUseCase criarUsuarioUseCase;

    @Mock
    private ListarUsuariosUseCase listarUsuariosUseCase;

    @Mock
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Mock
    private DeletarUsuarioUseCase deletarUsuarioUseCase;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO usuarioDTO;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Teste");
        usuarioDTO.setEmail("teste@email.com");
        usuarioDTO.setSenha("senha123");

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("Teste");
        usuarioEntity.setEmail("teste@email.com");
        usuarioEntity.setSenhaHash("hashedPassword");
        usuarioEntity.setDataCriacao(LocalDateTime.now());
        usuarioEntity.setDataAtualizacao(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        when(criarUsuarioUseCase.criarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioEntity);

        ResponseEntity<UsuarioEntity> response = usuarioController.criarUsuario(usuarioDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(usuarioEntity.getId(), response.getBody().getId());
        assertEquals(usuarioEntity.getNome(), response.getBody().getNome());
        assertEquals(usuarioEntity.getEmail(), response.getBody().getEmail());

        verify(criarUsuarioUseCase).criarUsuario(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar usuário com email duplicado")
    void deveRetornarErroAoCriarUsuarioComEmailDuplicado() {
        when(criarUsuarioUseCase.criarUsuario(any(UsuarioDTO.class)))
                .thenThrow(new EmailJaCadastradoException("Email já cadastrado"));

        assertThrows(EmailJaCadastradoException.class, () -> usuarioController.criarUsuario(usuarioDTO));

        verify(criarUsuarioUseCase).criarUsuario(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodosUsuarios() {
        List<UsuarioEntity> usuarios = Arrays.asList(usuarioEntity);
        when(listarUsuariosUseCase.listarUsuarios()).thenReturn(usuarios);

        ResponseEntity<List<UsuarioEntity>> response = usuarioController.listarUsuarios();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(usuarioEntity.getId(), response.getBody().get(0).getId());

        verify(listarUsuariosUseCase).listarUsuarios();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        when(listarUsuariosUseCase.buscarPorId(1L)).thenReturn(Optional.of(usuarioEntity));

        ResponseEntity<UsuarioEntity> response = usuarioController.buscarPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(usuarioEntity.getId(), response.getBody().getId());

        verify(listarUsuariosUseCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 quando usuário não encontrado")
    void deveRetornar404QuandoUsuarioNaoEncontrado() {
        when(listarUsuariosUseCase.buscarPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioEntity> response = usuarioController.buscarPorId(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(listarUsuariosUseCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        when(atualizarUsuarioUseCase.atualizarUsuario(eq(1L), any(UsuarioDTO.class)))
                .thenReturn(Optional.of(usuarioEntity));

        ResponseEntity<UsuarioEntity> response = usuarioController.atualizarUsuario(1L, usuarioDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(usuarioEntity.getId(), response.getBody().getId());

        verify(atualizarUsuarioUseCase).atualizarUsuario(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        doNothing().when(deletarUsuarioUseCase).deletarUsuario(1L);

        ResponseEntity<Void> response = usuarioController.deletarUsuario(1L);

        assertEquals(204, response.getStatusCodeValue());

        verify(deletarUsuarioUseCase).deletarUsuario(1L);
    }

    @Test
    @DisplayName("Deve retornar erro ao deletar usuário inexistente")
    void deveRetornarErroAoDeletarUsuarioInexistente() {
        doThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"))
                .when(deletarUsuarioUseCase).deletarUsuario(1L);

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioController.deletarUsuario(1L));

        verify(deletarUsuarioUseCase).deletarUsuario(1L);
    }
} 