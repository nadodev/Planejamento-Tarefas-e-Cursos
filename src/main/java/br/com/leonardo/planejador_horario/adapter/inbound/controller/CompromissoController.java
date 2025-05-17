package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.adapter.inbound.dto.CursoDTO;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.CriarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.impl.CriarCompromissoUseCaseImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compromissos")
@Tag(name = "Compromissos", description = "API para gerenciamento de compromissos")
@SecurityRequirement(name = "bearerAuth")
public class CompromissoController {

    private final CriarCompromissoUseCase criarCompromissoUseCase;

    public CompromissoController(CriarCompromissoUseCase criarCompromissoUseCase) {
        this.criarCompromissoUseCase = criarCompromissoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CompromissoDTO request) {
        try {
            CompromissoDTO cursoCriado = criarCompromissoUseCase.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CompromissoDTO>> listar() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompromissoDTO> buscarPorId(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompromissoDTO> atualizar(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CompromissoDTO compromisso
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }
}

