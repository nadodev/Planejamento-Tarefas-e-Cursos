package br.com.leonardo.planejador_horario.config;

import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Prioridade;
import br.com.leonardo.planejador_horario.domain.entities.Tarefa.Status;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Planejador de Horário API")
                .description("""
                    API para gerenciamento de tarefas e planejamento de horários.
                    
                    ## Enums Disponíveis
                    
                    ### Status
                    - PENDENTE: Tarefa ainda não iniciada
                    - EM_ANDAMENTO: Tarefa em execução
                    - CONCLUIDA: Tarefa finalizada
                    - CANCELADA: Tarefa cancelada
                    
                    ### Prioridade
                    - BAIXA: Prioridade baixa
                    - MEDIA: Prioridade média
                    - ALTA: Prioridade alta
                    - URGENTE: Prioridade urgente
                    
                    ### Dia da Semana
                    - SEGUNDA
                    - TERCA
                    - QUARTA
                    - QUINTA
                    - SEXTA
                    - SABADO
                    - DOMINGO
                    """)
                .version("1.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .servers(Arrays.asList(
                new Server().url("http://localhost:9090").description("Servidor Local")
            ));
    }

    @Bean
    public OpenApiCustomizer sortTagsAlphabetically() {
        return openApi -> {
            List<Tag> tags = openApi.getTags();
            if (tags != null) {
                tags.sort((tag1, tag2) -> tag1.getName().compareTo(tag2.getName()));
            }
        };
    }
} 