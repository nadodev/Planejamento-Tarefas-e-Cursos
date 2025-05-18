package br.com.leonardo.planejador_horario.integration;

import br.com.leonardo.planejador_horario.domain.entities.Usuario;
import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.domain.repository.UsuarioRepository;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
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

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DisponibilidadeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;
    private DisponibilidadeRequest request;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // Criar usuário de teste
        usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");
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

        // Criar request de teste
        request = new DisponibilidadeRequest();
        request.setDiaSemana(DiaSemana.SEGUNDA);
        request.setHoraInicio(LocalTime.of(9, 0));
        request.setHoraFim(LocalTime.of(17, 0));
    }

    @Test
    void criar_DeveRetornarDisponibilidadeCriada() throws Exception {
        mockMvc.perform(post("/api/disponibilidade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaSemana").value(request.getDiaSemana().toString()))
                .andExpect(jsonPath("$.horaInicio").value(request.getHoraInicio().toString()))
                .andExpect(jsonPath("$.horaFim").value(request.getHoraFim().toString()));
    }

    @Test
    void criar_SemToken_DeveRetornarUnauthorized() throws Exception {
        mockMvc.perform(post("/api/disponibilidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void criar_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        request.setHoraFim(LocalTime.of(8, 0)); // Hora fim antes da hora início

        mockMvc.perform(post("/api/disponibilidade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPorUsuario_DeveRetornarLista() throws Exception {
        // Criar disponibilidade primeiro
        mockMvc.perform(post("/api/disponibilidade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/disponibilidade/usuario/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].diaSemana").value(request.getDiaSemana().toString()));
    }

    @Test
    void listarPorUsuarioEDia_DeveRetornarLista() throws Exception {
        // Criar disponibilidade primeiro
        mockMvc.perform(post("/api/disponibilidade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/disponibilidade/usuario/" + usuario.getId() + "/dia/" + request.getDiaSemana())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].diaSemana").value(request.getDiaSemana().toString()));
    }

    @Test
    void excluir_DeveRetornarNoContent() throws Exception {
        // Criar disponibilidade primeiro
        String response = mockMvc.perform(post("/api/disponibilidade")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/disponibilidade/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Verificar se foi realmente excluída
        mockMvc.perform(get("/api/disponibilidade/usuario/" + usuario.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
} 