package br.com.leonardo.planejador_horario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {


        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("API Planejador de Horário")
                            .version("1.0")
                            .description("Documentação completa da API"));
        }

        @Bean
        public GroupedOpenApi allApi() {
            return GroupedOpenApi.builder()
                    .group("all-endpoints")
                    .packagesToScan("br.com.leonardo.planejador_horario")
                    .pathsToMatch("/**")
                    .build();
        }

    @Bean
    public OpenApiCustomizer customOpenApi() {
            return openApi -> {
                openApi.getPaths().values().forEach(pathItem -> {
                    pathItem.readOperations().forEach(operation -> {
                        if (operation.getTags() == null) {
                            operation.addTagsItem("default");
                        }
                    });
                });
            };
    }
}