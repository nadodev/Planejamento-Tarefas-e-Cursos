package br.com.leonardo.planejador_horario.config.security;

import br.com.leonardo.planejador_horario.PlanejadorHorarioApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PlanejadorHorarioApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void devePermitirAcessoRotaPublica() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarAcessoRotaAutenticadaSemToken() throws Exception {
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirAcessoRotaAutenticadaComToken() throws Exception {
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarAcessoRotaAutenticadaComTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/cursos")
                        .header("Authorization", "Bearer token_invalido"))
                .andExpect(status().isUnauthorized());
    }
} 