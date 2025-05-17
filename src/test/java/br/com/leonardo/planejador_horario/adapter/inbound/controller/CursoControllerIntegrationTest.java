package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.adapter.outbound.entity.UsuarioEntity;
import br.com.leonardo.planejador_horario.adapter.outbound.persistence.JpaCursoRepository;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaCursoRepository cursoRepository;

    @Autowired
    private JpaUsuarioRepository usuarioRepository;

    private UsuarioEntity usuario;
    private CursoDTO cursoDTO;

    @BeforeEach
    void setUp() {
        // Limpa o banco de dados de teste
        cursoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Cria um usuário para os testes
        usuario = new UsuarioEntity();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("123456");
        usuario = usuarioRepository.save(usuario);

        // Prepara o DTO do curso
        cursoDTO = new CursoDTO();
        cursoDTO.setNome("Curso de Teste");
        cursoDTO.setCargaHoraria(40);
        cursoDTO.setPrioridade(3);
        cursoDTO.setPrazoFinal(LocalDate.now().plusMonths(1));
        cursoDTO.setUsuarioId(usuario.getId());
    }

    @Test
    @DisplayName("Deve criar um curso com sucesso")
    void deveCriarCursoComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is(cursoDTO.getNome())))
                .andExpect(jsonPath("$.cargaHoraria", is(cursoDTO.getCargaHoraria())))
                .andExpect(jsonPath("$.prioridade", is(cursoDTO.getPrioridade())));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar curso com usuário inexistente")
    void deveRetornarErroAoCriarCursoComUsuarioInexistente() throws Exception {
        // Arrange
        cursoDTO.setUsuarioId(999L);

        // Act & Assert
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    @DisplayName("Deve listar todos os cursos")
    void deveListarTodosCursos() throws Exception {
        // Arrange
        // Cria um curso no banco
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)));

        // Act & Assert
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nome", is(cursoDTO.getNome())));
    }

    @Test
    @DisplayName("Deve listar cursos por usuário")
    void deveListarCursosPorUsuario() throws Exception {
        // Arrange
        // Cria um curso no banco
        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)));

        // Act & Assert
        mockMvc.perform(get("/api/cursos/usuario/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].nome", is(cursoDTO.getNome())));
    }

    @Test
    @DisplayName("Deve deletar curso com sucesso")
    void deveDeletarCursoComSucesso() throws Exception {
        // Arrange
        // Cria um curso no banco
        ResultActions createResult = mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cursoDTO)));

        String responseContent = createResult.andReturn().getResponse().getContentAsString();
        Long cursoId = objectMapper.readTree(responseContent).get("id").asLong();

        // Act & Assert
        mockMvc.perform(delete("/api/cursos/delete/" + cursoId))
                .andExpect(status().isNoContent());

        // Verifica se o curso foi realmente deletado
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
} 