package br.com.leonardo.planejador_horario.adapter.inbound.controller;

import br.com.leonardo.planejador_horario.adapter.inbound.dto.CompromissoDTO;
import br.com.leonardo.planejador_horario.domain.exception.CompromissoNaoEncontradoException;
import br.com.leonardo.planejador_horario.domain.exception.UsuarioNaoEncontradoException;
import br.com.leonardo.planejador_horario.usecase.compromisso.AtualizarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.CriarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.DeletarCompromissoUseCase;
import br.com.leonardo.planejador_horario.usecase.compromisso.ListarCompromissoUseCase;
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
    private final ListarCompromissoUseCase listarCompromissoUseCase;
    private final AtualizarCompromissoUseCase atualizarCompromissoUseCase;
    private final DeletarCompromissoUseCase deletarCompromissoUseCase;

    public CompromissoController(
            CriarCompromissoUseCase criarCompromissoUseCase,
            ListarCompromissoUseCase listarCompromissoUseCase,
            AtualizarCompromissoUseCase atualizarCompromissoUseCase,
            DeletarCompromissoUseCase deletarCompromissoUseCase
    ) {
        this.criarCompromissoUseCase = criarCompromissoUseCase;
        this.listarCompromissoUseCase = listarCompromissoUseCase;
        this.atualizarCompromissoUseCase = atualizarCompromissoUseCase;
        this.deletarCompromissoUseCase = deletarCompromissoUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar novo compromisso", description = "Cria um novo compromisso para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compromisso criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CompromissoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> criar(@Valid @RequestBody CompromissoDTO request) {
        try {
            CompromissoDTO compromissoCriado = criarCompromissoUseCase.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(compromissoCriado);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Listar compromissos", description = "Lista todos os compromissos ou os compromissos de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compromissos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = CompromissoDTO.class)))
    })
    public ResponseEntity<List<CompromissoDTO>> listar(
            @Parameter(description = "ID do usuário para filtrar compromissos")
            @RequestParam(required = false) Long usuarioId
    ) {
        List<CompromissoDTO> compromissos = usuarioId != null ?
                listarCompromissoUseCase.listarPorUsuario(usuarioId) :
                listarCompromissoUseCase.listarTodos();
        return ResponseEntity.ok(compromissos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar compromisso por ID", description = "Retorna um compromisso específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compromisso encontrado",
                    content = @Content(schema = @Schema(implementation = CompromissoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Compromisso não encontrado")
    })
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            CompromissoDTO compromisso = listarCompromissoUseCase.buscarPorId(id);
            return ResponseEntity.ok(compromisso);
        } catch (CompromissoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar compromisso", description = "Atualiza um compromisso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compromisso atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CompromissoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Compromisso ou usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CompromissoDTO compromisso
    ) {
        try {
            CompromissoDTO compromissoAtualizado = atualizarCompromissoUseCase.atualizar(id, compromisso);
            return ResponseEntity.ok(compromissoAtualizado);
        } catch (CompromissoNaoEncontradoException | UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar compromisso", description = "Remove um compromisso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Compromisso deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Compromisso não encontrado")
    })
    public ResponseEntity<?> deletar(
            @Parameter(description = "ID do compromisso", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            deletarCompromissoUseCase.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (CompromissoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao processar requisição: " + e.getMessage()));
        }
    }
}

