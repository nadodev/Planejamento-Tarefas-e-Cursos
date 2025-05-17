package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.UsuarioDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaUsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaUsuarioRepository usuarioRepository;

    private UsuarioDTO usuarioDTO;
    private UsuarioEntity usuarioExistente;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Usuário Teste");
        usuarioDTO.setEmail("teste@email.com");
        usuarioDTO.setSenha("senha123");

        usuarioExistente = new UsuarioEntity();
        usuarioExistente.setNome("Usuário Existente");
        usuarioExistente.setEmail("existente@email.com");
        usuarioExistente.setSenhaHash("senhaHash123");
        usuarioExistente = usuarioRepository.save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve criar um usuário e retornar 201")
    void deveCriarUsuarioERetornar201() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioDTO.getEmail()))
                .andExpect(jsonPath("$.senhaHash").exists())
                .andExpect(jsonPath("$.dataCriacao").exists())
                .andExpect(jsonPath("$.dataAtualizacao").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar usuário com dados inválidos")
    void deveRetornar400AoCriarUsuarioComDadosInvalidos() throws Exception {
        usuarioDTO.setEmail("email-invalido");
        usuarioDTO.setSenha("123"); // senha muito curta
        String jsonRequest = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar usuário com email duplicado")
    void deveRetornar400AoCriarUsuarioComEmailDuplicado() throws Exception {
        usuarioDTO.setEmail(usuarioExistente.getEmail());
        String jsonRequest = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar usuários e retornar 200")
    void deveListarUsuariosERetornar200() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].nome").exists())
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID e retornar 200")
    void deveBuscarUsuarioPorIdERetornar200() throws Exception {
        mockMvc.perform(get("/api/usuarios/{id}", usuarioExistente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioExistente.getId()))
                .andExpect(jsonPath("$.nome").value(usuarioExistente.getNome()))
                .andExpect(jsonPath("$.email").value(usuarioExistente.getEmail()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar usuário inexistente")
    void deveRetornar404AoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/api/usuarios/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar usuário e retornar 200")
    void deveAtualizarUsuarioERetornar200() throws Exception {
        usuarioDTO.setNome("Nome Atualizado");
        String jsonRequest = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(put("/api/usuarios/{id}", usuarioExistente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    @DisplayName("Deve deletar usuário e retornar 204")
    void deveDeletarUsuarioERetornar204() throws Exception {
        mockMvc.perform(delete("/api/usuarios/{id}", usuarioExistente.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/usuarios/{id}", usuarioExistente.getId()))
                .andExpect(status().isNotFound());
    }
} 