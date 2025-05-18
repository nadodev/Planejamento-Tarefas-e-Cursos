package br.com.leonardo.planejador_horario.integration;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.TarefaDTO;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TarefaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private TarefaDTO tarefaDTO;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // Criar usuário de teste
        usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("senha123");
        usuario = usuarioRepository.save(usuario);

        // Fazer login para obter token
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"teste@email.com\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = objectMapper.readTree(loginResponse).get("token").asText();

        // Criar tarefa de teste
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
    void criarTarefa_DeveRetornarTarefaCriada() throws Exception {
        mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(tarefaDTO.getTitulo()))
                .andExpect(jsonPath("$.status").value(tarefaDTO.getStatus().toString()))
                .andExpect(jsonPath("$.prioridade").value(tarefaDTO.getPrioridade().toString()));
    }

    @Test
    void criarTarefa_SemToken_DeveRetornarUnauthorized() throws Exception {
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void criarTarefa_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        tarefaDTO.setTitulo(""); // Título vazio

        mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarTarefas_DeveRetornarListaPaginada() throws Exception {
        // Criar tarefa primeiro
        mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)));

        mockMvc.perform(get("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void buscarTarefaPorId_DeveRetornarTarefa() throws Exception {
        // Criar tarefa primeiro
        String response = mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/tarefas/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value(tarefaDTO.getTitulo()));
    }

    @Test
    void buscarTarefaPorId_Inexistente_DeveRetornarNotFound() throws Exception {
        mockMvc.perform(get("/api/tarefas/999")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarTarefa_DeveRetornarTarefaAtualizada() throws Exception {
        // Criar tarefa primeiro
        String response = mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();
        tarefaDTO.setTitulo("Título Atualizado");

        mockMvc.perform(put("/api/tarefas/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título Atualizado"));
    }

    @Test
    void excluirTarefa_DeveRetornarNoContent() throws Exception {
        // Criar tarefa primeiro
        String response = mockMvc.perform(post("/api/tarefas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tarefas/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Verificar se foi realmente excluída
        mockMvc.perform(get("/api/tarefas/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
} 