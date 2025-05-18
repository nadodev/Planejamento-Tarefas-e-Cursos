package br.com.leonardo.planejador_horario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "br.com.leonardo.planejador_horario.adapter.outbound.repository",
    "br.com.leonardo.planejador_horario.adapter.outbound.persistence"
})
@EntityScan(basePackages = "br.com.leonardo.planejador_horario.adapter.outbound.entity")
public class PlanejadorHorarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanejadorHorarioApplication.class, args);
    }

}
