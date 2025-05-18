package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.domain.enums.DiaSemana;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeRequest;
import br.com.leonardo.planejador_horario.dto.DisponibilidadeResponse;
import br.com.leonardo.planejador_horario.services.DisponibilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
@Tag(name = "Disponibilidade", description = "Operações relacionadas a disponibilidade de horários")
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    public DisponibilidadeController(DisponibilidadeService disponibilidadeService) {
        this.disponibilidadeService = disponibilidadeService;
    }

    @PostMapping("/usuarios/{usuarioId}")
    public ResponseEntity<DisponibilidadeResponse> criar(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
            @Valid @RequestBody DisponibilidadeRequest request) {
        return ResponseEntity.ok(disponibilidadeService.criar(usuarioId, request));
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<List<DisponibilidadeResponse>> listarPorUsuario(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(disponibilidadeService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuarios/{usuarioId}/dias/{diaSemana}")
    public ResponseEntity<List<DisponibilidadeResponse>> listarPorUsuarioEDia(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long usuarioId,
            @Parameter(
                description = "Dia da semana",
                schema = @Schema(
                    implementation = DiaSemana.class,
                    allowableValues = {"SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"}
                )
            ) @PathVariable DiaSemana diaSemana) {
        return ResponseEntity.ok(disponibilidadeService.listarPorUsuarioEDia(usuarioId, diaSemana));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da disponibilidade", example = "1") @PathVariable Long id) {
        disponibilidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
} 